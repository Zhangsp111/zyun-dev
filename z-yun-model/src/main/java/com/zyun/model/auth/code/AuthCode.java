package com.zyun.model.auth.code;

import com.zyun.framework.response.ResultCode;
import lombok.AllArgsConstructor;

/**
 * @ClassName AuthCode
 * @Author: zsp
 * @Date 2021/3/18 21:39
 * @Description:
 * @Version 1.0
 */
@AllArgsConstructor
public enum AuthCode implements ResultCode {

    AUTH_USERNAME_NOT_EXISTS(12999, false, "请输入您的账号！"),
    AUTH_PASSWORD_NOT_EXISTS(12998, false, "请输入您的密码！"),
    AUTH_LOGIN_SUCCESS(12000, true, "登录成功！"),
    AUTH_LOGOUT_SUCCESS(12001, true, "您已退出登陆..."),
    AUTH_REFRESH_SUCCESS(12003, true, "刷新登陆凭证成功..."),
    AUTH_REMOTE_LOGIN_FAIL(12996, false, "远程请求校验失败！"),
    AUTH_CREDENTIAL_ERROR(12995, false,"账号或密码错误！"),
    AUTH_ACCOUNT_NOT_EXISTS(12994, false, "登陆凭证不存在！"),
    AUTH_TOKEN_SAVE_ERROR(12993, false, "登陆凭证保存失败！"),
    AUTH_GET_COOKIE_FAILED(12992, false, "获取COOKIE失败！"),
    AUTH_GET_ACCESS_TOKEN_FAILED(12991, false, "获取访问凭证失败！"),
    AUTH_LOGOUT_FAILED(12990, false, "登出失败！"),
    AUTH_GET_USER_ID_FAILED(12997, false, "获取用户id失败！"),
    AUTH_REMOVE_REDIS_TOKEN_FAILED(12989, false, "清除缓存凭证失败！"),
    ;
    int code;
    boolean success;
    String message;

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int code() {
        return code;
    }
}
