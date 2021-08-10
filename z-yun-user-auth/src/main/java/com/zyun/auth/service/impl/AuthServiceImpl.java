package com.zyun.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.zyun.auth.service.AuthService;
import com.zyun.framework.constant.ServerListConstant;
import com.zyun.framework.exception.ZYunException;
import com.zyun.framework.exception.ZYunExceptionCast;
import com.zyun.framework.utils.redis.RedisUtil;
import com.zyun.model.auth.code.AuthCode;
import com.zyun.model.auth.model.AuthToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName AuthServiceImpl
 * @Author: zsp
 * @Date 2021/3/16 12:11
 * @Description:
 * @Version 1.0
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.clientId}")
    private String webAppName;

    @Value("${auth.clientSecret}")
    private String webAppSecret;
    // 访问token的过期时间
    @Value("${auth.tokenValiditySeconds}")
    private Long tokenValiditySeconds;
    // 刷新token的过期时间
    @Value("${auth.RefreshTokenValiditySeconds}")
    private Long refreshTokenValiditySeconds;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final String GET_TOKEN_URL = "/oauth/token";

    private static final String GRANT_TYPE_PASSWORD = "password";

    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    private static final String TOKEN_SAVE_PREFIX = "auth_token:";

    @Autowired
    private RedisUtil<String, String> redisUtils;

    @Override
    public AuthToken login(String username, String password) {
        //1, 获取账户名和密码进行校验
        MultiValueMap<String, String> requestBody = getLoginFormMap(username, password);
        // 校验
        AuthToken authToken = check(requestBody);
        if (authToken == null || StringUtils.isEmpty(authToken.getAccessToken()))
            ZYunExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
        //2, 校验成功，将token保存到redis
        Long expireTimes = saveToken(authToken);
        if (expireTimes <= 0)
            ZYunExceptionCast.cast(AuthCode.AUTH_TOKEN_SAVE_ERROR);
        //3, 返回结果
        return authToken;
    }

    /**
     * 根据用户uid获取token
     * @param uid 用户身份jti
     * @return
     */
    @Override
    public String getJwt(String uid) {
        return redisUtils.get(TOKEN_SAVE_PREFIX + uid);
    }

    /**
     * 登出
     * @param jti 用户身份id
     * @return token过期日期
     */
    @Override
    public Long logout(String jti) {
        // 删除
        redisUtils.delete(TOKEN_SAVE_PREFIX + jti);
        //返回过期时间
        return redisUtils.getExpire(TOKEN_SAVE_PREFIX + jti);
    }

    @Override
    public AuthToken refreshToken(String oldCookie) {
        //1, 从redis获取用户原来的token
        String oldToken = redisUtils.get(TOKEN_SAVE_PREFIX + oldCookie);
        if (StringUtils.isEmpty(oldToken))
            ZYunExceptionCast.cast(AuthCode.AUTH_GET_ACCESS_TOKEN_FAILED);
        AuthToken authToken = JSON.parseObject(oldToken, AuthToken.class);
        //1, 请求数据刷新用户token
        MultiValueMap<String, String> body = getRefreshTokenMap(authToken.getRefreshToken());
        AuthToken refreshToken = check(body);
        //2, 先删除原来的token
        redisUtils.delete(TOKEN_SAVE_PREFIX + oldCookie);
        if (redisUtils.getExpire(TOKEN_SAVE_PREFIX + oldCookie) > 0)
            ZYunExceptionCast.cast(AuthCode.AUTH_REMOVE_REDIS_TOKEN_FAILED);
        //3, 再将新token保存到redis, 这里将token设置为refreshToken的保存时间
        String newJti = refreshToken.getJti();
        redisUtils.setForTimeCustom(TOKEN_SAVE_PREFIX + newJti,
                JSON.toJSONString(refreshToken),
                refreshTokenValiditySeconds,
                TimeUnit.SECONDS);
        return refreshToken;
    }

    /**
     * 组装用户用来刷新token的请求体数据
     * @param refreshToken 刷新token
     */
    private MultiValueMap<String, String> getRefreshTokenMap(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", GRANT_TYPE_REFRESH_TOKEN);
        body.add("client_id", webAppName);
        body.add("client_secret", webAppSecret);
        body.add("refresh_token", refreshToken);
        return body;
    }

    /**
     * 组装用户用来登陆请求token的请求体数据
     */
    private MultiValueMap<String, String> getLoginFormMap(String username, String password) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", GRANT_TYPE_PASSWORD);
        body.add("username", username);
        body.add("password", password);
        return body;
    }

    /**
     * 将token保存到redis
     *   将refresh的保存时间射为access的两倍
     * @return 保存token的过期时间
     */
    private Long saveToken(AuthToken authToken) {
        String tokenKey = TOKEN_SAVE_PREFIX + authToken.getJti();
        redisUtils.setForTimeCustom(tokenKey,
                JSON.toJSONString(authToken), refreshTokenValiditySeconds, TimeUnit.SECONDS);
        return redisUtils.getExpire(tokenKey);
    }

    /**
     * 用户登陆和刷新token使用同一个接口
     * 不过接口中参数设置不同，在此处使用不同的body进行请求
     * @param body 请求数据体
     */
    private AuthToken check(MultiValueMap<String, String> body) {
        //1，采用客户端负载均衡，从eureka获取认证服务的ip 和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(ServerListConstant.Z_YUN_USER_AUTH_SERVER);
        URI clientUri = serviceInstance.getUri();
        String authUrl = clientUri + contextPath + GET_TOKEN_URL;
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        //2，对应用名和密码进行编码并填充授权访问参数 格式：Basic WGNXZWJBcHA6WGNXZWJBcHA=
        String httpBasic = httpBasic(webAppName, webAppSecret);
        headers.add("Authorization", httpBasic);
        //3，组装密码方式登陆的访问参数
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity
                = new HttpEntity<MultiValueMap<String, String>>(body, headers);
        //4, 指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值，之后远程请求校验
        Map resultMap = null;
        try {
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                    //当响应的值为400或401时候也要正常响应，不要抛出异常
                    if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                        super.handleError(response);
                    }
                }
            });
            ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
            resultMap = exchange.getBody();
        } catch (Exception e) {
            ZYunExceptionCast.cast(AuthCode.AUTH_REMOTE_LOGIN_FAIL);
        }
        // 校验失败返回账号或密码错误等信息
        if (resultMap == null ||
                resultMap.get("access_token") == null ||
                resultMap.get("refresh_token") == null ||
                resultMap.get("jti") == null) {
            // 获取失败信息
            if (resultMap != null
                    || !org.springframework.util.StringUtils.isEmpty(resultMap.get("error_description"))) {
                String errorDescription = (String) resultMap.get("error_description");
                if (errorDescription.contains("坏的凭证")) {
                    ZYunExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                } else if (errorDescription.contains("Cannot pass null or empty values to constructor")) {
                    ZYunExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOT_EXISTS);
                }
            }
        }

        // 5,返回校验成功获取到的token
        return AuthToken.builder()
                .accessToken(resultMap.get("access_token").toString())
                .jti(resultMap.get("jti").toString())
                .refreshToken(resultMap.get("refresh_token").toString()).build();
    }

    /**
     * 编码
     * @param webAppName
     * @param webAppSecret
     * @return
     */
    private String httpBasic(String webAppName, String webAppSecret) {
        String headerContent = webAppName + ":" + webAppSecret;
        byte[] encode = Base64Utils.encode(headerContent.getBytes());
        return "Basic " + new String(encode);
    }



}
