package com.zyun.framework.response;

/**
 * @ClassName CommonCode
 * @Author: zsp
 * @Date 2021/3/17 21:34
 * @Description:
 * @Version 1.0
 */
public enum CommonCode implements ResultCode {

    SUCCESS(10000, true, "操作成功!"),
    FAIL(19999, false, "操作失败!"),
    INVALID_PARAM(19998, false, "参数有误！"),
    SERVER_ERROR(19997, false, "服务器未响应！"),
    NO_AUTH_ERROR(19996, false, "未授权！"),
    ;

    CommonCode(int code, boolean success, String message) {
        this.code = code;
        this.success = success;
        this.message = message;
    }

    /**
     * 定义枚举类的属性
     */
    int code;
    boolean success;
    String message;

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int code() {
        return code;
    }
}
