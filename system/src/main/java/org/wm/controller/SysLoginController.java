package org.wm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wm.authentication.SysLoginService;
import org.wm.authentication.SysPermissionService;
import org.wm.constants.Constants;
import org.wm.entity.SysMenu;
import org.wm.entity.SysUser;
import org.wm.entity.vo.LoginBody;
import org.wm.entity.vo.RouterVo;
import org.wm.response.ResponseResult;
import org.wm.service.ISysMenuService;
import org.wm.utils.SecurityUtils;


/**
 * 登录验证
 *
 * @author sk
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public ResponseResult<Map<String, Object>> login(@RequestBody LoginBody loginBody) {
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TOKEN, token);
        return ResponseResult.success(map);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public ResponseResult<Map<String, Object>> getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
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
    public ResponseResult<List<RouterVo>> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return ResponseResult.success(menuService.buildMenus(menus));
    }
}
