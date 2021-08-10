package com.zyun.framework.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName QueryResult
 * @Author: zsp
 * @Date 2021/3/17 21:52
 * @Description:
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QueryResult<T> {

    /**
     * 数据集合
     */
    private List<T> list;

    /**
     * 数量
     */
    private Long total;

}
