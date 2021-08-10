package com.zyun.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.zyun.framework.response.CommonCode;
import com.zyun.framework.response.ResponseResult;
import com.zyun.framework.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ExceptionHandler
 * @Author: zsp
 * @Date 2021/3/17 22:09
 * @Description:
 * @Version 1.0
 */
@ControllerAdvice
public class ZYunExceptionHandler {

    /**
     * 定义日志处理
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZYunExceptionHandler.class);
    /**
     * Guava包 定义map，配置异常类型所对应的错误代码，此处并未初始化
     */
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    /**
     * Guava包 定义map的builder对象，去构建ImmutableMap
     */
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    /**
     * 捕获基础异常
     */
    @ExceptionHandler(ZYunException.class)
    @ResponseBody
    public ResponseResult customException(ZYunException customException){
        customException.printStackTrace();
        //记录日志
        LOGGER.error("catch exception:{}",customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    /**
     * 定义多种异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception){
        exception.printStackTrace();
        //记录日志
        LOGGER.error("catch exception:{}",exception.getMessage());
        // 初始化构建EXCEPTIONS集合
        if(EXCEPTIONS == null){
            EXCEPTIONS = builder.build();
        }
        // 从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应99999异常
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if(resultCode !=null){
            return new ResponseResult(resultCode);
        }else{
            // 返回服务器异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }

    static {
        //定义异常类型所对应的错误代码
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }

}
