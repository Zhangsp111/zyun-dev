package com.zyun.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.zyun.framework.exception.ZYunExceptionCast;
import com.zyun.framework.utils.redis.RedisUtil;
import com.zyun.model.system.code.SystemCode;
import com.zyun.model.system.constant.SystemConstant;
import com.zyun.model.system.user.User;
import com.zyun.model.system.user.ext.UserExt;
import com.zyun.system.repository.SystemMenuMapper;
import com.zyun.system.repository.SystemUserMapper;
import com.zyun.system.repository.UserRepository;
import com.zyun.system.service.UserManagerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserManagerServiceImpl
 * @Author: zsp
 * @Date 2021/3/16 12:39
 * @Description:
 * @Version 1.0
 */
@Service
public class UserManagerServiceImpl implements UserManagerService {

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @Override
    public UserExt getByUsername(String username) {
        if (StringUtils.isEmpty(username))
            ZYunExceptionCast.cast(SystemCode.SYSTEM_GET_USERNAME_ERROR);
        UserExt userExt = systemUserMapper.selectUserInfoByUsername(username);
        if (userExt == null || userExt.getId() == null)
            ZYunExceptionCast.cast(SystemCode.SYSTEM_GET_USER_INFO_ERROR);
        Integer roleId = userExt.getRoleId();
        if (roleId != null) {
            List<String> permissions = systemMenuMapper.selectPermissionsByRoleId(roleId);
            userExt.setPermissions(permissions);
        }
        return userExt;
    }
}
