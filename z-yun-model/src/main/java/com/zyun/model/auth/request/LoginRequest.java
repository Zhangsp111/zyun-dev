package com.zyun.model.auth.request;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName LoginRequest
 * @Author: zsp
 * @Date 2021/3/18 21:27
 * @Description:
 * @Version 1.0
 */
@Data
@ToString
public class LoginRequest {

    private String username;

    private String password;

}
