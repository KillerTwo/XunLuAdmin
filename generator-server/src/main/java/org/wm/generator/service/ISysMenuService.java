package org.wm.generator.service;

import org.wm.generator.domain.SysMenu;

import java.util.List;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2024/01/14 00:23
 * @since 1.0
**/
public interface ISysMenuService {

    /**
     * 功能描述：<功能描述>
     *       按条件查询菜单
     * @author dove
     * @date 2024/1/14 00:36
     * @param sysMenu
     * @return java.util.List<org.wm.generator.domain.SysMenu>
     * @throws
     */
    List<SysMenu> selectSysMenuList(SysMenu sysMenu);


    /**
     * 功能描述：<功能描述>
     *       查询所有菜单
     * @author dove
     * @date 2024/1/14 00:36
     * @return java.util.List<org.wm.generator.domain.SysMenu>
     * @throws
     */
    List<SysMenu> selectAll();


    /**
     * 功能描述：<功能描述>
     *       查询子菜单
     * @author dove
     * @date 2024/1/14 00:36
     * @param parentId
     * @return java.util.List<org.wm.generator.domain.SysMenu>
     * @throws
     */
    List<SysMenu> selectChildren(Long parentId);

    /**
     * 功能描述：<功能描述>
     *       新增菜单
     * @author dove
     * @date 2024/1/14 00:36
     * @param sysMenu
     * @return int
     * @throws
     */
    int insertMenu(SysMenu sysMenu);

    /**
     * 功能描述：<功能描述>
     *       修改菜单
     * @author dove
     * @date 2024/1/14 00:36
     * @param sysMenu
     * @return int
     * @throws
     */
    int updateMenu(SysMenu sysMenu);

}
