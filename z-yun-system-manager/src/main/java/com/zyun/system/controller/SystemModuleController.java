package com.zyun.system.controller;

import com.zyun.api.system.SystemModuleControllerApi;
import com.zyun.framework.controller.BaseController;
import com.zyun.framework.exception.ZYunExceptionCast;
import com.zyun.framework.response.CommonCode;
import com.zyun.model.auth.code.AuthCode;
import com.zyun.model.system.module.SystemModule;
import com.zyun.model.system.response.SystemResult;
import com.zyun.system.service.SystemModuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName SystemModuleController
 * @Author: zsp
 * @Date 2021/4/12 20:23
 * @Description:
 * @Version 1.0
 */
@RestController
@RequestMapping("/module")
public class SystemModuleController extends BaseController implements SystemModuleControllerApi {

    @Autowired
    private SystemModuleService systemModuleService;

    /**
     * 根据权限id获取模块信息
     * @return
     */
    @GetMapping("/getByRoleId")
    //@PreAuthorize("hasAuthority('SYSTEM:MENU')")
    @Override
    public List<SystemModule> getModuleByFlag() {
        Integer roleId = super.getRoleId();
        if (roleId == null)
            ZYunExceptionCast.cast(AuthCode.AUTH_GET_USER_ID_FAILED);
        return systemModuleService.getModulesByUserId(roleId);
    }

    /**
     * 模块信息
     * @return
     */
    @GetMapping("/modules")
    @Override
    public SystemResult<List<SystemModule>> getModules() {
        List<SystemModule> systemModules = systemModuleService.getModules();
        if (systemModules == null || systemModules.size() <= 0)
            return new SystemResult<>(CommonCode.FAIL);
        return new SystemResult<>(CommonCode.SUCCESS, systemModules);
    }
}
