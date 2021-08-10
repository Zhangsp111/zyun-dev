package com.zyun.model.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName QueryVo
 * @Author: zsp
 * @Date 2021/4/18 18:42
 * @Description:
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryVo implements Serializable {

    private String keywords;

    @NotNull(message = "参数不能为空！")
    private Integer pageSize;

    @NotNull(message = "参数不能为空！")
    private Integer pageNum;


}
