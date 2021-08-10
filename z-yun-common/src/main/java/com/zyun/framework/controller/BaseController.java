package com.zyun.framework.controller;

import com.zyun.framework.utils.Oauth2Util;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @ClassName BaseController
 * @Author: zsp
 * @Date 2021/3/20 20:20
 * @Description:
 * @Version 1.0
 */
public class BaseController {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession session;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    /**
     * 获取用户信息
     * @return
     */
    protected String getUserId() {
        Map<String, String> jwtClaims = Oauth2Util.getJwtClaimsFromHeader(request);
        if (jwtClaims == null || StringUtils.isEmpty(jwtClaims.get("id"))) {
            return null;
        }
        return jwtClaims.get("id");
    }

    /**
     * 获取用户信息
     * @return
     */
    protected Integer getRoleId() {
        Map<String, String> jwtClaims = Oauth2Util.getJwtClaimsFromHeader(request);
        if (jwtClaims == null || StringUtils.isEmpty(jwtClaims.get("roleId"))) {
            return null;
        }
        return Integer.parseInt(jwtClaims.get("roleId"));
    }

}
