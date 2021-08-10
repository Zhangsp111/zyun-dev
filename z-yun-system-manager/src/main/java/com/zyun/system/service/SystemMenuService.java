package com.zyun.system.service;

import com.zyun.model.system.menu.SystemMenu;
import com.zyun.model.system.menu.ext.SystemMenuExt;
import com.zyun.model.system.response.SystemQueryResult;

import java.util.List;

/**
 * @ClassName SystemMenuService
 * @Author: zsp
 * @Date 2021/4/14 20:02
 * @Description:
 * @Version 1.0
 */
public interface SystemMenuService {

    /**
     * 获取所有菜单
     * @return
     */
    SystemQueryResult<SystemMenuExt> getMenus(Integer pageSize, Integer pageNum, String keywords);

    /**
     * 根据用户权限和模块id获取菜单
     * @param roleId
     * @param moduleId
     * @return
     */
    List<SystemMenuExt> getMenusById(Integer roleId, Integer moduleId);

    /**
     * 获取路由
     * @param roleId
     * @return
     */
    List<SystemMenu> getRoutes(Integer roleId);

    /**
     * 更新启用标识
     * @param systemMenu
     * @return
     */
    int updateFlagById(SystemMenu systemMenu);

    /**
     *
     * @param menuId
     * @return
     */
    SystemMenuExt getByMenuId(String menuId);

    /**
     * 修改菜单信息
     * @param systemMenu
     * @return
     */
    int updateMenuById(SystemMenu systemMenu, String userId);

    /**
     * 根据id删除菜单
     * @param ids
     * @return
     */
    int delByIds(String ids);
}
