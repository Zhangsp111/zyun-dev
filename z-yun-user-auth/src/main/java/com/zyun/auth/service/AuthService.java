package com.zyun.auth.service;

import com.zyun.model.auth.model.AuthToken;
import com.zyun.model.auth.request.LoginRequest;
import com.zyun.model.auth.response.LoginResult;

/**
 * @ClassName AuthService
 * @Author: zsp
 * @Date 2021/3/16 12:11
 * @Description:
 * @Version 1.0
 */
public interface AuthService {

    /**
     * 登陆接口
     */
    AuthToken login(String username, String password);

    /**
     * 根据登陆凭证获取登陆token
     * @param uid 用户身份jti
     * @return
     */
    String getJwt(String uid);

    /**
     * 登出
     * @param jti
     * @return
     */
    Long logout(String jti);

    /**
     * 刷新用户token
     * @param uidCookie
     * @return
     */
    AuthToken refreshToken(String uidCookie);
}
