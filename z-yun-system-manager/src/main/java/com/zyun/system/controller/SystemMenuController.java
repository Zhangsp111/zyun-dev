package com.zyun.system.controller;

import com.zyun.api.system.SystemMenuControllerApi;
import com.zyun.framework.controller.BaseController;
import com.zyun.framework.exception.ZYunExceptionCast;
import com.zyun.framework.response.CommonCode;
import com.zyun.model.system.code.SystemCode;
import com.zyun.model.system.menu.SystemMenu;
import com.zyun.model.system.menu.ext.SystemMenuExt;
import com.zyun.model.system.response.SystemQueryResult;
import com.zyun.model.system.response.SystemResult;
import com.zyun.model.system.vo.SystemQueryVo;
import com.zyun.system.service.SystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName SystemMenuController
 * @Author: zsp
 * @Date 2021/4/14 19:48
 * @Description:
 * @Version 1.0
 */
@RestController
@RequestMapping("/menu")
public class SystemMenuController extends BaseController
        implements SystemMenuControllerApi {

    @Autowired
    private SystemMenuService systemMenuService;

    /**
     * 根据模块id获取所有模块若为空则获取所有
     * @param moduleId
     * @return
     */
    @GetMapping("/getById/{id}")
    @Override
    public List<SystemMenuExt> getMenusByModuleId(@PathVariable(value = "id") Integer moduleId) {
        Integer roleId = getRoleId();
        if (roleId == null || moduleId == null)
            ZYunExceptionCast.cast(SystemCode.SYSTEM_GET_USER_ROLE_ERROR);
        return systemMenuService.getMenusById(roleId, moduleId);
    }

    /**
     * 获取路由
     * @return
     */
    @GetMapping("/routes")
    @Override
    public List<SystemMenu> getRoutes() {
        Integer roleId = getRoleId();
        return systemMenuService.getRoutes(roleId);
    }

    @GetMapping("/menus")
    @Override
    public SystemQueryResult<SystemMenuExt> getMenus(@Valid SystemQueryVo systemQueryVo) {
        Integer pageNum = systemQueryVo.getPageNum();
        Integer pageSize = systemQueryVo.getPageSize();
        if (pageNum < 0)
            pageNum = 0;
        if (pageSize < 0)
            pageSize = 10;
        return systemMenuService.getMenus(pageSize, pageNum, systemQueryVo.getKeywords());
    }

    @PutMapping("/updateFlag")
    @Override
    public SystemResult<SystemMenu> updateFlag(@Valid @RequestBody SystemMenu systemMenu) {
        int i = systemMenuService.updateFlagById(systemMenu);
        if (i <= 0) return new SystemResult<>(CommonCode.FAIL);
        return new SystemResult<>(CommonCode.SUCCESS);
    }

    @GetMapping("/getByMenuId/{id}")
    @Override
    public SystemResult<SystemMenuExt> getById(@PathVariable("id") String menuId) {
        SystemMenuExt systemMenu = systemMenuService.getByMenuId(menuId);
        if (systemMenu == null)
            return new SystemResult<>(CommonCode.FAIL);
        return new SystemResult<>(CommonCode.SUCCESS, systemMenu);
    }

    /**
     * 修改菜单信息
     * @param systemMenu
     * @return
     */
    @PutMapping("/update")
    @Override
    public SystemResult<SystemMenu> updateMenuById(@Valid @RequestBody SystemMenu systemMenu) {
        String userId = getUserId();
        int i = systemMenuService.updateMenuById(systemMenu, userId);
        if (i <= 0)
            return new SystemResult<>(CommonCode.FAIL);
        return new SystemResult<>(CommonCode.SUCCESS);
    }

    /**
     * 获取12级菜单
     */
    @GetMapping("/getParents")
    @Override
    public SystemResult<List<SystemMenuExt>> getParents() {
        // 为null则全查
        List<SystemMenuExt> menus = systemMenuService.getMenusById(null, null);
        if (menus.size() == 0)
            return new SystemResult<>(SystemCode.SYSTEM_MENU_GET_ERROR);
        return new SystemResult<>(CommonCode.SUCCESS, menus);
    }

    @DeleteMapping("/delById")
    @Override
    public SystemResult<SystemMenu> delById(@RequestParam("ids") String ids) {
        if (systemMenuService.delByIds(ids) == 0)
            return new SystemResult<>(CommonCode.FAIL);
        return new SystemResult<>(CommonCode.SUCCESS);
    }
}