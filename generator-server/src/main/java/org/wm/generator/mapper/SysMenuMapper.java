package org.wm.generator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.wm.generator.domain.SysMenu;

import java.util.List;

/**
 * 功能描述：<功能描述>
 * 菜单Mapper
 * @author dove 
 * @date 2024/01/13 23:08
 * @since 1.0
**/
@Mapper
public interface SysMenuMapper {


    /**
     * 功能描述：<功能描述>
     *       多条件查询菜单
     * @author dove
     * @date 2024/1/14 00:06
     * @param menu
     * @return java.util.List<org.wm.generator.domain.SysMenu>
     * @throws
     */
    List<SysMenu> selectSysMenuList(SysMenu menu);


    /**
     * 功能描述：<功能描述>
     *       查询指定菜单的子菜单
     * @author dove
     * @date 2024/1/14 00:07
     * @param parentId
     * @return java.util.List<org.wm.generator.domain.SysMenu>
     * @throws
     */
    List<SysMenu> selectSysMenuListByParent(Long parentId);


    /**
     * 功能描述：<功能描述>
     *       查询全部菜单
     * @author dove
     * @date 2024/1/14 00:07
     * @return java.util.List<org.wm.generator.domain.SysMenu>
     * @throws
     */
    List<SysMenu> selectSysMenuAll();

    /**
     * 功能描述：<功能描述>
     *       新增菜单
     * @author dove
     * @date 2024/1/14 00:07
     * @param menu
     * @return org.wm.generator.domain.SysMenu
     * @throws
     */
    int insertSysMenu(SysMenu menu);


    /**
     * 功能描述：<功能描述>
     *       更新菜单
     * @author dove
     * @date 2024/1/14 00:07
     * @param sysMenu
     * @return int
     * @throws
     */
    int updateSysMenu(SysMenu sysMenu);


    /**
     * 功能描述：<功能描述>
     *       批量删除菜单
     * @author dove
     * @date 2024/1/14 00:07
     * @param ids
     * @return int
     * @throws
     */
    int deleteSysMenuIds(Long[] ids);

}
