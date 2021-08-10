package com.zyun.model.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AuthToken
 * @Author: zsp
 * @Date 2021/3/18 22:03
 * @Description:
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {

    private String accessToken;

    private String refreshToken;

    /**
     * 保存身份信息
     */
    private String jti;

}
