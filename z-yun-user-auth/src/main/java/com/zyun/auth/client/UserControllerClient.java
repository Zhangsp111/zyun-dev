package com.zyun.auth.client;

import com.zyun.framework.constant.ServerListConstant;
import com.zyun.model.system.user.ext.UserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName UserControllerClient
 * @Author: zsp
 * @Date 2021/3/16 17:39
 * @Description:
 * @Version 1.0
 */
@FeignClient(name = ServerListConstant.Z_YUN_SYSTEM_MANAGER_SERVER)
public interface UserControllerClient {

    @GetMapping("/system/user/getByUsername")
    UserExt getUserByUsername(@RequestParam("username") String username);

}
