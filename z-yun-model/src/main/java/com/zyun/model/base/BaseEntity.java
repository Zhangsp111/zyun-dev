package com.zyun.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import java.util.Date;

/**
 * @ClassName BaseEntity
 * @Author: zsp
 * @Date 2021/4/20 21:22
 * @Description:
 * @Version 1.0
 */
public class BaseEntity {

    @JsonIgnore
    @Column(name = "create_date")
    private Date createDate;
    @JsonIgnore
    @Column(name = "create_user")
    private String createUser;
    @JsonIgnore
    @Column(name = "update_date")
    private Date updateTime;
    @JsonIgnore
    @Column(name = "update_user")
    private String updateUser;


}
