package com.zyun.api.system;

import com.zyun.model.system.user.ext.UserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName UserManagerService
 * @Author: zsp
 * @Date 2021/3/16 12:29
 * @Description:
 * @Version 1.0
 */
@Api("用户管理")
public interface UserManagerControllerApi {

    @ApiOperation("根据用户名称获取用户信息")
    UserExt getUserByUsername(String username);

}
