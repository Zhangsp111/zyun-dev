package com.zyun.model.system.menu;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName SystemMenu
 * @Author: zsp
 * @Date 2021/4/12 12:34
 * @Description:
 * @Version 1.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "zyun_menu")
public class SystemMenu {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    public String id;

    @NotNull(message = "启用标识不能为空！")
    public String flag;

    @NotNull(message = "菜单名称不能为空！")
    public String menuName;

    public String parentId;

    @NotNull(message = "模块名称不能为空！")
    public Integer moduleId;

    @NotNull(message = "模块图标不能为空！")
    public String icon;

    public String path;
    /* 授权码 */
    @NotNull(message = "授权码不能为空！")
    public String menuCode;

    public String level;
    @JsonIgnore
    public Date createTime;
    @JsonIgnore
    public String createUser;
    @JsonIgnore
    public Date updateTime;
    @JsonIgnore
    public String updateUser;

}
