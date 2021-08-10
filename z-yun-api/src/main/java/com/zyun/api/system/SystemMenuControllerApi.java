package com.zyun.api.system;

import com.zyun.model.system.menu.SystemMenu;
import com.zyun.model.system.menu.ext.SystemMenuExt;
import com.zyun.model.system.response.SystemQueryResult;
import com.zyun.model.system.response.SystemResult;
import com.zyun.model.system.vo.SystemQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @ClassName SystemMenuControllerApi
 * @Author: zsp
 * @Date 2021/4/14 19:46
 * @Description:
 */
@Api("菜单管理模块")
public interface SystemMenuControllerApi {

    /**
     * 获取菜单
     * @return
     */
    List<SystemMenuExt> getMenusByModuleId(Integer moduleId);

    /**
     * 获取子路由（1级菜单）
     */
    List<SystemMenu> getRoutes();

    /**
     * 查询所有菜单
     */
    SystemQueryResult<SystemMenuExt> getMenus(SystemQueryVo systemQueryVo);

    /**
     * 更新启用标识
     */
    SystemResult<SystemMenu> updateFlag(SystemMenu systemMenu);

    /**
     * 根据id获取菜单信息
     */
    @ApiOperation("根据菜单id获取菜单信息")
    SystemResult<SystemMenuExt> getById(String menuId);

    @ApiOperation("根据修改菜单信息")
    SystemResult updateMenuById(SystemMenu systemMenu);

    @ApiOperation("获取1-2级菜单")
    SystemResult<List<SystemMenuExt>> getParents();

    @ApiOperation("根据Id删除菜单")
    SystemResult<SystemMenu> delById(String ids);

}
