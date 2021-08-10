package com.zyun.gateway.config;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zyun.framework.response.CommonCode;
import com.zyun.framework.utils.CookieUtil;
import com.zyun.framework.utils.redis.RedisUtil;
import com.zyun.model.auth.response.LoginResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName LoginFilter
 * @Author: zsp
 * @Date 2021/3/21 21:01
 * @Description:
 * @Version 1.0
 */
@Configuration
public class LoginFilter extends ZuulFilter {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 返回字符串代表过滤器的类型，如下
     *      pre：请求在被路由之前 执行
     *      routing：在路由请求时调用
     *      post：在routing和errror过滤器之后调用
     *      error：处理请求时发生错误调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 此方法返回整型数值，通过此数值来定义过滤器的执行顺序，数字越小优先级越高。
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 返回一个Boolean值，判断该过滤器是否需要执行。返回true表示要执行此过滤器，否则不执行。
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 未在该环节设置响应，过滤器便会通过
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String cookie = this.getCookie(request);
        if (StringUtils.isEmpty(cookie))
            refuseAccess();

        String authString = this.getTokenFromHeaders(request);
        if (StringUtils.isEmpty(authString))
            refuseAccess();

        if (StringUtils.isNotEmpty(cookie) && this.getUidExpireFromRedis(cookie) <= 0)
            refuseAccess();
        return null;
    }

    /**
     * 获取cookie的过期时间
     * @param cookie
     * @return
     */
    private Long getUidExpireFromRedis(String cookie) {
        return redisUtil.getExpire("auth_token:" + cookie);
    }

    /**
     * 从请求头中获取授权信息
     * @param request
     * @return
     */
    private String getTokenFromHeaders(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization) || !authorization.startsWith("Bearer "))
            return null;
        return authorization.substring(7);
    }

    /**
     * 从请求中获取cookie
     * @param request
     * @return
     */
    private String getCookie(HttpServletRequest request) {
        Map<String, String> cookie = CookieUtil.readCookie(request, "uid");
        return cookie.get("uid");
    }

    /**
     * 拒绝访问方法
     */
    private void refuseAccess() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 拒绝请求
        requestContext.setResponseStatusCode(200);//设置响应码
        requestContext.setSendZuulResponse(false);//拒绝访问
        // 设置响应
        LoginResult loginResult = new LoginResult(CommonCode.NO_AUTH_ERROR);
        String jsonString = JSON.toJSONString(loginResult);
        requestContext.setResponseBody(jsonString);
        //获取响应
        HttpServletResponse response = requestContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
    }

}
