package com.zyun.auth.controller;

import com.zyun.api.auth.AuthControllerApi;
import com.zyun.auth.service.AuthService;
import com.zyun.framework.controller.BaseController;
import com.zyun.framework.exception.ZYunExceptionCast;
import com.zyun.framework.response.CommonCode;
import com.zyun.framework.utils.CookieUtil;
import com.zyun.model.auth.code.AuthCode;
import com.zyun.model.auth.model.AuthToken;
import com.zyun.model.auth.request.LoginRequest;
import com.zyun.model.auth.response.JwtResult;
import com.zyun.model.auth.response.LoginResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName AuthController
 * @Author: zsp
 * @Date 2021/3/16 12:09
 * @Description:
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class AuthController extends BaseController implements AuthControllerApi {

    @Autowired
    private AuthService authService;

    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    private int maxAge;

    private static final String AUTH_COOKIE_KEY = "uid";

    private static final String AUTH_COOKIE_PATH = "/";


    /**
     * 登陆接口
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    @Override
    public LoginResult login(LoginRequest loginRequest) {
        if (loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername()))
            ZYunExceptionCast.cast(AuthCode.AUTH_USERNAME_NOT_EXISTS);
        if (StringUtils.isEmpty(loginRequest.getPassword()))
            ZYunExceptionCast.cast(AuthCode.AUTH_PASSWORD_NOT_EXISTS);
        AuthToken authToken = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        // 将token回写到cookie，回写身份信息，即jti，因为jti比较短
        saveToCookie(authToken.getJti());
        return new LoginResult(AuthCode.AUTH_LOGIN_SUCCESS);
    }

    /**
     * 刷新token,并将新的身份信息重写到redis并返回到cookie
     */
    @PostMapping("/refresh")
    @Override
    public LoginResult refresh() {
        //1，从cookie中获取到用户身份信息 uid=***
        Map<String, String> uidCookies = CookieUtil.readCookie(request, AUTH_COOKIE_KEY);
        String uidCookie = uidCookies.get(AUTH_COOKIE_KEY);
        if (StringUtils.isEmpty(uidCookie))
            ZYunExceptionCast.cast(AuthCode.AUTH_GET_COOKIE_FAILED);
        //2, 请求service刷新cookie
        AuthToken authToken = authService.refreshToken(uidCookie);
        //3, 将重新获取到的cookie写回到domain中
        CookieUtil.addCookie(response,
                cookieDomain,
                AUTH_COOKIE_PATH,
                AUTH_COOKIE_KEY,
                authToken.getJti(),
                maxAge,
                false);
        return new LoginResult(AuthCode.AUTH_REFRESH_SUCCESS, authToken.getAccessToken());
    }

    /**
     * 将token回写到cookie
     */
    private void saveToCookie(String userJti) {
        HttpServletResponse response = null;
        try {
            response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        } catch (NullPointerException e) {
            ZYunExceptionCast.cast(AuthCode.AUTH_GET_COOKIE_FAILED);
        }
        CookieUtil.addCookie(response,
                cookieDomain,
                AUTH_COOKIE_PATH,
                AUTH_COOKIE_KEY,
                userJti,
                maxAge,
                false);
    }

    @PostMapping("/logout")
    @Override
    public LoginResult logout() {
        String jti = this.getTokenFromCookie(request);
        Long expireTimes = authService.logout(jti);
        if (expireTimes >= 0)
            ZYunExceptionCast.cast(AuthCode.AUTH_LOGOUT_FAILED);
        //将jti从cookie中删除
        CookieUtil.addCookie(response,
                cookieDomain,
                AUTH_COOKIE_PATH,
                AUTH_COOKIE_KEY,
                null,
                maxAge,
                false);
        return new LoginResult(AuthCode.AUTH_LOGOUT_SUCCESS);
    }

    /**
     * 从cookie中获取用户身份信息(jti)，并依靠Jti获取access_token
     * 若在获取jwt过程中发现用户token过期则请求刷新token
     * @return
     */
    @GetMapping("/jwt")
    @Override
    public JwtResult getJwt() {
        String jti = this.getTokenFromCookie(request);
        String accessToken = authService.getJwt(jti);
        if (StringUtils.isEmpty(accessToken))
            ZYunExceptionCast.cast(AuthCode.AUTH_GET_ACCESS_TOKEN_FAILED);
        return new JwtResult(CommonCode.SUCCESS, accessToken);
    }

    /**
     * 从cookie中获取jti
     * @return
     */
    private String getTokenFromCookie(HttpServletRequest request) {
        Map<String, String> cookies = CookieUtil.readCookie(request, AUTH_COOKIE_KEY);
        return cookies.get(AUTH_COOKIE_KEY);
    }

}
