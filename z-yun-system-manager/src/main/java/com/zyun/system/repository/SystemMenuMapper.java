package com.zyun.system.repository;

import com.zyun.model.system.menu.SystemMenu;
import com.zyun.model.system.menu.ext.SystemMenuExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName SystemMenuMapper
 * @Author: zsp
 * @Date 2021/4/12 12:41
 * @Description:
 * @Version 1.0
 */
@Mapper
@Repository
public interface SystemMenuMapper {

    /**
     * 根据角色id查询角色所拥有的菜单权限
     * @param roleId
     * @return
     */
    List<String> selectPermissionsByRoleId(@Param("roleId") Integer roleId);

    /**
     * 查询菜单
     * @return
     */
    List<SystemMenuExt> selectMenus(@Param("keyword") String keyword);

    /**
     * 根据权限和模块id获取菜单
     * @param roleId
     * @param moduleId
     * @return
     */
    List<SystemMenuExt> selectMenusByRoleIdAndModuleId(@Param("roleId") Integer roleId,
                                                       @Param("moduleId") Integer moduleId);

    /**
     * 获取路由
     * @param roleId
     * @return
     */
    List<SystemMenu> selectLevel2(@Param("roleId") Integer roleId);

    /**
     * 查询层级为1的记录数量
     * @return
     */
    Long selectCountByLevel1();

    /**
     * 根据id更新标识
     * @param id
     * @param flag
     * @return
     */
    int updateFlagById(@Param("id") String id, @Param("flag") String flag);

    /**
     * 根据菜单id获取菜单信息
     * @param menuId
     * @return
     */
    SystemMenuExt selectMenuById(@Param("menuId") String menuId);

    /**
     * 根据id删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") String[] ids);
}
