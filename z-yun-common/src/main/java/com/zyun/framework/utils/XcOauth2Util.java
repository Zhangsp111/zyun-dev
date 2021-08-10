package com.zyun.framework.utils;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName XcOauth2Util
 * @Author: zsp
 * @Date 2021/4/12 21:38
 * @Description:
 * @Version 1.0
 */
public class XcOauth2Util {

    /**
     * 获取用户id
     *
     * @param request
     * @return
     */
    public static String getUserJwtFromHeader(HttpServletRequest request) {
        Map<String, String> jwtClaims = Oauth2Util.getJwtClaimsFromHeader(request);
        if (jwtClaims == null || StringUtils.isEmpty(jwtClaims.get("id"))) {
            return null;
        }
        return jwtClaims.get("id");
    }

}
