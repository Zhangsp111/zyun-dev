package com.zyun.system.repository;

import com.zyun.model.system.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName UserRepository
 * @Author: zsp
 * @Date 2021/3/16 12:40
 * @Description:
 * @Version 1.0
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User getByUsername(String username);

}
