package com.zyun.auth.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

/**
 * @ClassName UserJwt
 * @Author: zsp
 * @Date 2021/3/16 12:19
 * @Description:
 * @Version 1.0
 */
@Data
@ToString
public class UserJwt extends User implements Serializable {

    // 用户id
    private String id;
    // 用户名称
    private String name;

    private Integer roleId;

    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}
