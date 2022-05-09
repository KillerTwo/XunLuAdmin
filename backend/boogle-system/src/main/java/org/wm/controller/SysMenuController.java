package org.wm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wm.constants.UserConstants;
import org.wm.entity.SysMenu;
import org.wm.entity.vo.TreeSelect;
import org.wm.response.ResponseResult;
import org.wm.service.ISysMenuService;
import org.wm.utils.StringUtils;


/**
 * 菜单信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public ResponseResult<List<SysMenu>> list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return ResponseResult.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public ResponseResult<SysMenu> getInfo(@PathVariable Long menuId) {
        return ResponseResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public ResponseResult<List<TreeSelect>> treeselect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return ResponseResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public ResponseResult<Map<String, Object>> roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysMenu> menus = menuService.selectMenuList(getUserId());
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ResponseResult.success(ajax);
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @PostMapping
    public ResponseResult<?> add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return ResponseResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return ResponseResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PutMapping
    public ResponseResult<?> edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return ResponseResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return ResponseResult.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return ResponseResult.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @DeleteMapping("/{menuId}")
    public ResponseResult<?> remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return ResponseResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return ResponseResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}