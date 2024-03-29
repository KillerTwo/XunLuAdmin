package org.wm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.wm.commons.constants.UserConstants;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.response.PageResult;
import org.wm.commons.response.ResponseResult;
import org.wm.commons.utils.StringUtils;
import org.wm.commons.web.controller.BaseController;
import org.wm.security.utils.SecurityUtils;
import org.wm.system.entity.SysRole;
import org.wm.system.entity.SysUser;
import org.wm.system.entity.SysUserRole;
import org.wm.system.service.ISysRoleService;
import org.wm.system.service.ISysUserService;
import org.wm.system.service.SysPermissionService;
import org.wm.token.TokenParser;


import java.util.List;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenParser tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    // @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public PageResult<SysRole> list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    /**
     * 根据角色编号获取详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public ResponseResult<SysRole> getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return ResponseResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    // @PreAuthorize("@ss.hasPermi('system:role:add')")
    @PostMapping
    public ResponseResult<?> add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return ResponseResult.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return ResponseResult.error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(SecurityUtils.getUsername());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    // @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping
    public ResponseResult<?> edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return ResponseResult.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return ResponseResult.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(SecurityUtils.getUsername());

        if (roleService.updateRole(role) > 0) {
            // 更新缓存用户权限
            /*LoginUser loginUser = SecurityUtils.getLoginUser();
            if (StringUtils.isNotNull(loginUser) && !loginUser.isAdmin()) {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                tokenService.setLoginUser(loginUser);
            }*/
            return ResponseResult.success();
        }
        return ResponseResult.error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
    // @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/dataScope")
    public ResponseResult<Integer> dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    // @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/changeStatus")
    public ResponseResult<Integer> changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    // @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @DeleteMapping("/{roleIds}")
    public ResponseResult<Integer> remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    // @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public ResponseResult<List<SysRole>> optionselect() {
        return ResponseResult.success(roleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     */
    // @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public PageResult<SysUser> allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
    // @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public PageResult<SysUser> unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
    // @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/authUser/cancel")
    public ResponseResult<Integer> cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
    // @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/authUser/cancelAll")
    public ResponseResult<Integer> cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    // @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/authUser/selectAll")
    public ResponseResult<Integer> selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }
}
