package com.zyun.api.system;

import com.zyun.model.system.module.SystemModule;
import com.zyun.model.system.response.SystemResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName SystemModelControllerApi
 * @Author: zsp
 * @Date 2021/3/14 21:42
 * @Description: 系统模块配置API
 * @Version 1.0
 */
@Api("首页模块管理中心")
public interface SystemModuleControllerApi {

    @ApiOperation("根据用户id获取用户首页模块")
    List<SystemModule> getModuleByFlag();

    @ApiOperation("获取所有菜单名称id")
    SystemResult<List<SystemModule>> getModules();

}
