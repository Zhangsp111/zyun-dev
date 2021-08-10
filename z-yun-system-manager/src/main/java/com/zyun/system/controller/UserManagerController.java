package com.zyun.system.controller;

import com.zyun.api.system.UserManagerControllerApi;
import com.zyun.model.system.user.ext.UserExt;
import com.zyun.system.service.UserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserManagerController
 * @Author: zsp
 * @Date 2021/3/16 12:32
 * @Description:
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserManagerController implements UserManagerControllerApi {

    @Autowired
    private UserManagerService userManagerService;

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @GetMapping("/getByUsername")
    @Override
    public UserExt getUserByUsername(@RequestParam("username") String username) {
        return userManagerService.getByUsername(username);
    }
}
