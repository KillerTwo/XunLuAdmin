package org.wm.generator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.wm.generator.domain.SysMenu;
import org.wm.generator.response.ResponseResult;
import org.wm.generator.service.ISysMenuService;

import java.util.List;

/**
 * 功能描述：<功能描述>
 * 菜单控制器
 * @author dove 
 * @date 2024/01/13 13:04
 * @since 1.0
**/
@RequiredArgsConstructor
@RestController
public class MenuController {


    private final ISysMenuService sysMenuService;

    @GetMapping("/listMenu")
    public ResponseResult<List<SysMenu>> listMenu() {
        var menuList = sysMenuService.listMenu();
        return ResponseResult.success(menuList);
    }

    @GetMapping("/getPermCode")
    public ResponseResult<List<String>> getPermCode() {
        return ResponseResult.success(List.of("1000", "3000", "5000"));
    }

    @RequestMapping("/children/{parentId}")
    public ResponseResult<List<SysMenu>> listMenuByParentId(@PathVariable("parentId") Long parentId) {
        var list = sysMenuService.selectChildren(parentId);
        return ResponseResult.success(list);
    }

    @PostMapping("/add/{parentId}")
    public ResponseResult<List<SysMenu>> addMenu(@PathVariable("parentId") Long parentId, @RequestBody SysMenu sysMenu) {
        var effect = sysMenuService.insertMenu(sysMenu);
        if (effect > 0) {
            var list = sysMenuService.selectChildren(parentId);
            return ResponseResult.success(list);
        }

        return ResponseResult.error("add error");
    }

}
