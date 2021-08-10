package com.zyun.model.system.code;

import com.zyun.framework.response.ResultCode;
import lombok.AllArgsConstructor;

/**
 * @ClassName AuthCode
 * @Author: zsp
 * @Date 2021/3/18 21:39
 * @Description:
 * @Version 1.0
 */
@AllArgsConstructor
public enum SystemCode implements ResultCode {

    SYSTEM_GET_USERNAME_ERROR(13999, false, "获取用户名称失败！"),
    SYSTEM_GET_USER_INFO_ERROR(13998, false, "获取用户信息失败！"),
    SYSTEM_GET_USER_ROLE_ERROR(13997, false, "获取用户权限失败！"),
    SYSTEM_MENU_NOT_FOUND_ERROR(13996, false, "该菜单不存在！"),
    SYSTEM_MENU_GET_ERROR(13995, false, "获取菜单信息失败！"),
    SYSTEM_MENU_IDS_NOT_FOUND(13994, false, "菜单id不存在！"),
    SYSTEM_MENU_DEL_ERROR(13993, false, "菜单删除失败！"),
    ;
    int code;
    boolean success;
    String message;

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int code() {
        return code;
    }
}
