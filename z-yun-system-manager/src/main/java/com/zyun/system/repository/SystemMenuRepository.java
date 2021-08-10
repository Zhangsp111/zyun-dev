package com.zyun.system.repository;

import com.zyun.model.system.menu.SystemMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SystemMenuRepository
 * @Author: zsp
 * @Date 2021/4/19 17:18
 * @Description:
 * @Version 1.0
 */
@Repository
public interface SystemMenuRepository extends CrudRepository<SystemMenu, String> {
}
