package com.zyun.model.system.menu.ext;

import com.zyun.model.system.menu.SystemMenu;
import lombok.Data;

import java.util.List;

/**
 * @ClassName SystemMenuExt
 * @Author: zsp
 * @Date 2021/4/12 12:39
 * @Description:
 * @Version 1.0
 */
@Data
public class SystemMenuExt extends SystemMenu {

    private List<SystemMenuExt> children;

    private String moduleName;

    private String parentName;

    /**
     * 是否拥有子节点
     */
    private boolean hasChildren;

}
