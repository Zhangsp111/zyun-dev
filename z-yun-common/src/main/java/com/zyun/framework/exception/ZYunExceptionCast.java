package com.zyun.framework.exception;

import com.zyun.framework.response.ResultCode;

/**
 * @ClassName ExceptionCast
 * @Author: zsp
 * @Date 2021/3/17 22:06
 * @Description:
 * @Version 1.0
 */
public class ZYunExceptionCast {

    /**
     * 抛出异常
     * @param resultCode
     */
    public static void cast(ResultCode resultCode) {
        throw new ZYunException(resultCode);
    }

}
