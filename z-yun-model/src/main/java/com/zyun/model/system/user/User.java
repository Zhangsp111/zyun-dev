package com.zyun.model.system.user;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName User
 * @Author: zsp
 * @Date 2021/3/14 20:49
 * @Description:
 * @Version 1.0
 */
@Data
@ToString
@Entity
@Table(name="zyun_user")
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    private String name;

}
