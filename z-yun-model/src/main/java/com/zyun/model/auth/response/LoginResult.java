package com.zyun.model.auth.response;

import com.zyun.framework.response.ResponseResult;
import com.zyun.framework.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName LoginResult
 * @Author: zsp
 * @Date 2021/3/18 21:27
 * @Description:
 * @Version 1.0
 */
@Data
@ToString
@NoArgsConstructor
public class LoginResult extends ResponseResult {

    private String token;

    public LoginResult(ResultCode resultCode, String token) {
        super(resultCode);
        this.token = token;
    }

    public LoginResult(ResultCode resultCode) {
        super(resultCode);
    }

}
