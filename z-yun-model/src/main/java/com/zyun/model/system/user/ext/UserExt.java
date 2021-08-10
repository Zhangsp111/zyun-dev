package com.zyun.model.system.user.ext;

import com.zyun.model.system.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName UserExt
 * @Author: zsp
 * @Date 2021/3/16 12:30
 * @Description: 用户信息扩展
 * @Version 1.0
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserExt extends User {

    /**
     * 用户头像
     */
    private String userPic;

    /**
     * 用户权限
     */
    private Integer roleId;

    /*用户权限*/
    private List<String> permissions;

}
