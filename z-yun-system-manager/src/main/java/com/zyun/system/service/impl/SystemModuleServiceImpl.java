package com.zyun.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.zyun.framework.utils.redis.RedisUtil;
import com.zyun.model.system.constant.SystemConstant;
import com.zyun.model.system.module.SystemModule;
import com.zyun.system.repository.SystemModuleMapper;
import com.zyun.system.repository.SystemModuleRepository;
import com.zyun.system.service.SystemModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SystemModuleServiceImpl
 * @Author: zsp
 * @Date 2021/4/12 20:26
 * @Description:
 * @Version 1.0
 */
@Service
public class SystemModuleServiceImpl implements SystemModuleService {

    @Autowired
    private SystemModuleMapper systemModuleMapper;

    @Autowired
    private SystemModuleRepository systemModuleRepository;


    @Override
    public List<SystemModule> getModulesByUserId(Integer roleId) {
        return systemModuleMapper.selectModulesByRoleId(roleId);
    }

    @Override
    public List<SystemModule> getModules() {
        return systemModuleRepository.findAll();
    }
}
