package com.zyun.system.repository;

import com.zyun.model.system.module.SystemModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SystemModuleRepository
 * @Author: zsp
 * @Date 2021/4/20 21:07
 * @Description:
 * @Version 1.0
 */
@Repository
public interface SystemModuleRepository extends JpaRepository<SystemModule, Integer> {



}
