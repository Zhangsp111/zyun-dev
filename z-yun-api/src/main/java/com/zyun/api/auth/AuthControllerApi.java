package com.zyun.api.auth;

import com.zyun.model.auth.request.LoginRequest;
import com.zyun.model.auth.response.JwtResult;
import com.zyun.model.auth.response.LoginResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName AuthControllerApi
 * @Author: zsp
 * @Date 2021/3/16 17:35
 * @Description:
 * @Version 1.0
 */
@Api("用户授权")
public interface AuthControllerApi {

    @ApiOperation("登陆接口")
    LoginResult login(LoginRequest loginRequest);

    @ApiOperation("登出接口")
    LoginResult logout();

    @ApiOperation("根据jwt获取用户信息")
    JwtResult getJwt();

    @ApiOperation("刷新用户token")
    LoginResult refresh();

}
