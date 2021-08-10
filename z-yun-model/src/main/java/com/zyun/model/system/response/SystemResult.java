package com.zyun.model.system.response;

import com.zyun.framework.response.ResponseResult;
import com.zyun.framework.response.ResultCode;
import com.zyun.model.system.menu.ext.SystemMenuExt;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName SystemMenuResult
 * @Author: zsp
 * @Date 2021/4/18 19:06
 * @Description:
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SystemResult<T> extends ResponseResult {

    private T data;

    public SystemResult(ResultCode resultCode, T t) {
        super(resultCode);
        this.data = t;
    }

    public SystemResult(ResultCode resultCode) {
        super(resultCode);
    }

}
