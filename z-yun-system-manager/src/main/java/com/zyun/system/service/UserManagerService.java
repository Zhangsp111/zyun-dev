package com.zyun.system.service;

import com.zyun.model.system.user.ext.UserExt;

/**
 * @ClassName UserManagerService
 * @Author: zsp
 * @Date 2021/3/16 12:39
 * @Description:
 * @Version 1.0
 */
public interface UserManagerService {

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    UserExt getByUsername(String username);

}
