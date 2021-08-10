package com.zyun.system.repository;

import com.zyun.model.system.user.ext.UserExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SystemUserMapper
 * @Author: zsp
 * @Date 2021/4/13 12:01
 * @Description:
 * @Version 1.0
 */
@Mapper
@Repository
public interface SystemUserMapper {

    /**
     * 根据用户名获取用户信息，权限及角色id
     */
    UserExt selectUserInfoByUsername(@Param("username") String username);

}
