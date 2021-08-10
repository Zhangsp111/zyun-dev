package com.zyun.model.system.response;

import com.zyun.framework.response.QueryResult;
import com.zyun.framework.response.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName SystemQueryResult
 * @Author: zsp
 * @Date 2021/4/19 12:23
 * @Description:
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemQueryResult<T> extends QueryResult<T> {

    private String message;

    private boolean success;

    private Integer code;


    public SystemQueryResult(List<T> t, Long total, ResultCode resultCode) {
        super(t, total);
        this.message = resultCode.message();
        this.success = resultCode.success();
        this.code = resultCode.code();
    }

}
