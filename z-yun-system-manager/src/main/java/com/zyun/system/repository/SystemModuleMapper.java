package com.zyun.system.repository;

import com.zyun.model.system.module.SystemModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName SystemModuleMapper
 * @Author: zsp
 * @Date 2021/4/12 19:22
 * @Description:
 * @Version 1.0
 */
@Mapper
@Repository
public interface SystemModuleMapper {

    List<SystemModule> selectModulesById(@Param("userId") String userId);

    List<SystemModule> selectModulesByRoleId(@Param("roleId") Integer roleId);

}
