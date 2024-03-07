package org.wm.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.wm.commons.response.ResponseResult;
import org.wm.security.utils.SecurityUtils;

import org.wm.system.entity.SysMenu;
import org.wm.system.entity.SysUser;
import org.wm.system.entity.vo.ResetPasswordBody;
import org.wm.system.entity.vo.RouterReactVo;
import org.wm.system.service.ISysMenuService;
import org.wm.system.service.ISysUserService;
import org.wm.system.service.SysPermissionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2024/01/25 00:08
 * @since 1.0
**/
@RequiredArgsConstructor
@RequestMapping("/system")
@RestController
public class CurrentUserController {


    private final SysPermissionService permissionService;

    private final ISysUserService sysUserService;

    private final ISysMenuService menuService;


    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public ResponseResult<?> getInfo() {
        SecurityUtils.getLoginUser();
        // SysUser user = SecurityUtils.getLoginUser().getUser();
        // var user = SecurityContextHolder.getContext().getAuthentication();
        var loginUser = SecurityUtils.getLoginUser();

        var user = new SysUser();
        user.setUserId(loginUser.getUserId());

        var dbUser = sysUserService.selectUserById(loginUser.getUserId());

        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", dbUser);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ResponseResult.success(ajax);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public ResponseResult<List<RouterReactVo>> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return ResponseResult.success(menuService.buildMenusReact(menus));
    }

    /**
     * 重置密码
     *
     * @param resetPasswordBody 请求参数
     * @return 结果
     */
    @PostMapping("/resetPassword")
    public ResponseResult<Map<String, Object>> resetPassword(@RequestBody ResetPasswordBody resetPasswordBody) {
        /*loginService.resetPassword(resetPasswordBody);
        return ResponseResult.success();*/
        return null;
    }

}
