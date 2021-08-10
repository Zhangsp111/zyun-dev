package com.zyun.model.auth.response;

import com.zyun.framework.response.ResponseResult;
import com.zyun.framework.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName JwtResult
 * @Author: zsp
 * @Date 2021/3/18 21:31
 * @Description:
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ToString
public class JwtResult extends ResponseResult {

    private String jwt;

    public JwtResult(ResultCode resultCode, String jwt) {
        super(resultCode);
        this.jwt = jwt;
    }

}
