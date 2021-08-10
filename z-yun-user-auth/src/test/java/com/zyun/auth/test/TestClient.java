package com.zyun.auth.test;

import com.netflix.discovery.converters.Auto;
import com.zyun.auth.service.AuthService;
import com.zyun.framework.constant.ServerListConstant;
import com.zyun.model.auth.request.LoginRequest;
import com.zyun.model.auth.response.LoginResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @ClassName TestClient
 * @Author: zsp
 * @Date 2021/3/3 21:34
 * @Description:
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired(required = false)
    RestTemplate restTemplate;
    @Test
    public void testClient(){
        //采用客户端负载均衡，从eureka获取认证服务的ip 和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(ServerListConstant.Z_YUN_USER_AUTH_SERVER);
        URI uri = serviceInstance.getUri();
        String authUrl = uri+"/auth/oauth/token";
        //URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType
        // url就是 申请令牌的url /oauth/token
        // method http的方法类型
        // requestEntity请求内容
        // responseType，将响应的结果生成的类型
        // 请求的内容分两部分
        // 1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        String httpbasic = httpbasic("ZYunWebApp", "123");
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization", httpbasic);
        //2、包括：grant_type、username、passowrd 密码方式
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type","password");
        body.add("username","admin");
        body.add("password","123");
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity
                = new HttpEntity<MultiValueMap<String, String>>(body, headers);
        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });
        //远程调用申请令牌
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        Map body1 = exchange.getBody();
        System.out.println(body1);
    }
    private String httpbasic(String clientId,String clientSecret){
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId+":"+clientSecret;
        //进行base64编
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+ new String(encode);
    }



    @Test
    public void testCrypt() {
        String password = "111111";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 10; i++) {
            String encode = passwordEncoder.encode(password);
            System.out.println(encode);

            boolean matches = passwordEncoder.matches(password, encode);
            System.out.println(matches);
        }
    }

    @Autowired
    private AuthService authService;

    @Test
    public void test11() {
        authService.login("admin", "123");
    }

}
