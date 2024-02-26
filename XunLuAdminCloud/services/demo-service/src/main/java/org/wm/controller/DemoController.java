package org.wm.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wm.commons.response.ResponseResult;
import org.wm.security.utils.SecurityUtils;


/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/07/25 23:11
 * @since 1.0
**/
@RestController
public class DemoController {


    @GetMapping("/hello")
    public ResponseResult<?> hello() {
        // var loginUser = SecurityUtils.getLoginUser();
        // return ResponseResult.success("ok", String.format("当前登录用户为%s", loginUser.getUsername()));
        var user = SecurityContextHolder.getContext().getAuthentication();
        return ResponseResult.success(user);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/testRole")
    public ResponseResult<?> testRole() {
        return ResponseResult.success("ok", "hello admin");
    }

}
