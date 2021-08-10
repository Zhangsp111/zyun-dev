package com.zyun.framework.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName QueryResponseResult
 * @Author: zsp
 * @Date 2021/3/17 21:54
 * @Description:
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    private QueryResult<T> queryResult;

    QueryResponseResult(ResultCode resultCode, QueryResult<T> queryResult) {
        super(resultCode);
        this.queryResult = queryResult;
    }

}
