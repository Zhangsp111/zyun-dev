package com.zyun.framework.response;

/**
 * @ClassName ResultCode
 * @Author: zsp
 * @Date 2021/3/17 21:31
 * @Description: 
 * @Version 1.0
 */
public interface ResultCode {

    boolean success();

    String message();

    int code();

}
