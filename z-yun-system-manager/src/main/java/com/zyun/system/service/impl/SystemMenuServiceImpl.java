package com.zyun.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyun.framework.exception.ZYunException;
import com.zyun.framework.exception.ZYunExceptionCast;
import com.zyun.framework.response.CommonCode;
import com.zyun.model.system.code.SystemCode;
import com.zyun.model.system.constant.SystemConstant;
import com.zyun.model.system.menu.SystemMenu;
import com.zyun.model.system.menu.ext.SystemMenuExt;
import com.zyun.model.system.response.SystemQueryResult;
import com.zyun.system.repository.SystemMenuMapper;
import com.zyun.system.repository.SystemMenuRepository;
import com.zyun.system.service.SystemMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName SystemMenuServiceImpl
 * @Author: zsp
 * @Date 2021/4/14 20:02
 * @Description:
 * @Version 1.0
 */
@Service
public class SystemMenuServiceImpl implements SystemMenuService {

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    private static final String MENUS_KEY = SystemConstant.SYSTEM_CACHE_PREFIX_KEY + "menu:";

    @Autowired
    private SystemMenuRepository systemMenuRepository;

    /**
     * 查询所有菜单
     * @return
     */
    @Override
    public SystemQueryResult<SystemMenuExt> getMenus(Integer pageSize, Integer pageNum, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<SystemMenuExt> menuExts = systemMenuMapper.selectMenus(keyword);
        Long total = null;
        if (menuExts.size() > 0)
            total = systemMenuMapper.selectCountByLevel1();
        menuExts.forEach(ext -> {
            List<SystemMenuExt> children = ext.getChildren();
            // 懒加载false
            if (children != null && children.size() >0) {
                ext.setHasChildren(false);
                children.forEach(chi -> {
                    if (chi.getChildren() != null && chi.getChildren().size() > 0) chi.setHasChildren(false);
                });
            }
        });
        PageInfo<SystemMenuExt> pageInfo = PageInfo.of(menuExts);
        return new SystemQueryResult<SystemMenuExt>(menuExts,
                total,
                CommonCode.SUCCESS);
    }

    /**
     * 根据用户权限和模块id查询菜单
     * @param roleId
     * @param moduleId
     * @return
     */
    @Override
    public List<SystemMenuExt> getMenusById(Integer roleId, Integer moduleId) {
        return systemMenuMapper.selectMenusByRoleIdAndModuleId(roleId, moduleId);
    }

    /**
     * 根据权限和模块Id生成redis key
     * @param roleId 权限Id
     * @param moduleId 模块Id
     * @return
     */
    private String getMenuKey(Integer roleId, Integer moduleId) {
        return  MENUS_KEY + "module_" + moduleId.toString() + ":role_" + roleId.toString();
    }

    /**
     * 获取路由
     * @param roleId
     * @return
     */
    @Override
    public List<SystemMenu> getRoutes(Integer roleId) {
        return systemMenuMapper.selectLevel2(roleId);
    }

    /**
     * 更新启用标识
     * @param systemMenu
     * @return
     */
    @Override
    public int updateFlagById(SystemMenu systemMenu) {
        int i = systemMenuMapper.updateFlagById(systemMenu.getId(), systemMenu.getFlag());
        return i;
    }

    /**
     * 根据菜单id获取菜单信息
     * @param menuId
     * @return
     */
    @Override
    public SystemMenuExt getByMenuId(String menuId) {
        return systemMenuMapper.selectMenuById(menuId);
    }

    /**
     * 更新菜单信息
     * @param systemMenu
     * @return
     */
    @Override
    public int updateMenuById(SystemMenu systemMenu, String userId) {
        // id不为空证明是更新
        SystemMenu save = null;
        if (StringUtils.isEmpty(systemMenu.getParentId()))
            systemMenu.setParentId("0");
        if (StringUtils.isEmpty(systemMenu.getLevel()))
            systemMenu.setLevel("1");
        if (StringUtils.isNotEmpty(systemMenu.getId())) {
            Optional<SystemMenu> optionalSystemMenu = systemMenuRepository.findById(systemMenu.getId());
            if (!optionalSystemMenu.isPresent())
                ZYunExceptionCast.cast(SystemCode.SYSTEM_MENU_NOT_FOUND_ERROR);
            SystemMenu oldMenu = optionalSystemMenu.get();
            systemMenu.setUpdateUser(userId);
            systemMenu.setUpdateTime(new Date());
            systemMenu.setCreateUser(oldMenu.getCreateUser());
            systemMenu.setCreateTime(oldMenu.getCreateTime());
        } else {
            //id为空证明是插入
            systemMenu.setCreateTime(new Date());
            systemMenu.setCreateUser(userId);
        }
        save = systemMenuRepository.save(systemMenu);
        return StringUtils.isNotEmpty(save.getId()) ? 1 : 0;
    }

    /**
     * 根据id删除菜单
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public int delByIds(String ids) {
        if (StringUtils.isEmpty(ids))
            ZYunExceptionCast.cast(SystemCode.SYSTEM_MENU_IDS_NOT_FOUND);
        String[] split = ids.split(",");
        int length = split.length;
        if (length == 0)
            ZYunExceptionCast.cast(SystemCode.SYSTEM_MENU_IDS_NOT_FOUND);
        // 根据id删除
        int count = systemMenuMapper.deleteByIds(split);
        if (count != length)
            ZYunExceptionCast.cast(SystemCode.SYSTEM_MENU_DEL_ERROR);
        return count;
    }
}
