package com.zyun.framework.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName ResponseResult
 * @Author: zsp
 * @Date 2021/3/17 21:45
 * @Description:
 * @Version 1.0
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult implements Response {

    private int code = SUCCESS_CODE;

    private String message = SUCCESS_MESSAGE;

    private boolean success = SUCCESS;

    public ResponseResult(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.success = resultCode.success();
    }

    public static ResponseResult success() {
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public static ResponseResult fail() {
        return new ResponseResult(CommonCode.FAIL);
    }

}
