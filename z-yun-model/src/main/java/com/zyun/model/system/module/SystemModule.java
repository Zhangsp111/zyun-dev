package com.zyun.model.system.module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName SystemModule
 * @Author: zsp
 * @Date 2021/3/20 21:04
 * @Description:
 * @Version 1.0
 */
@Entity
@Data
@ToString
@Table(name = "zyun_module")
@AllArgsConstructor
@NoArgsConstructor
public class SystemModule {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "module_type")
    @JsonIgnore
    private String moduleType;

    @JsonIgnore
    @Column(name = "flag")
    private String flag;

}





