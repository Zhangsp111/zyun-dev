package com.zyun.framework.exception;

import com.zyun.framework.response.ResultCode;
import lombok.Getter;

/**
 * @ClassName ZYunException
 * @Author: zsp
 * @Date 2021/3/17 22:03
 * @Description: 定义全局通用异常
 * @Version 1.0
 */
@Getter
public class ZYunException extends RuntimeException {

    ResultCode resultCode;

    public ZYunException(ResultCode resultCode) {
        super("错误代码: " + resultCode.code() + "，错误信息: " + resultCode.message());
        this.resultCode = resultCode;
    }

}
