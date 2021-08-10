package com.zyun.system.service;

import com.zyun.model.system.module.SystemModule;

import java.util.List;

/**
 * @ClassName SystemModuleService
 * @Author: zsp
 * @Date 2021/4/12 20:25
 * @Description:
 * @Version 1.0
 */
public interface SystemModuleService {

    /**
     * 根据用户id获取模块集合
     * @param roleId
     * @return
     */
    List<SystemModule> getModulesByUserId(Integer roleId);

    /**
     * 获取所有模块信息
     * @return
     */
    List<SystemModule> getModules();
}
