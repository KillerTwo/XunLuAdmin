package org.wm.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.wm.auth.feignClient.MenuServiceClient;
import org.wm.auth.feignClient.UserServiceClient;
import org.wm.auth.feignClient.demo.DemoServiceClient;
import org.wm.auth.params.LoginParam;
import org.wm.auth.service.AuthService;
import org.wm.commons.constants.SecurityConstants;
import org.wm.commons.response.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 功能描述：<功能描述>
 *
 *
 * @author dove
 * @date 2023/7/11 00:47
 * @since 1.0
 **/
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final MenuServiceClient menuServiceClient;

    private final UserServiceClient userServiceClient;

    private final AuthService authService;

    private final DemoServiceClient demoServiceClient;


    /**
     * 功能描述：<功能描述>
     *     用户登录，获取TOKEN
     *
     * @author dove
     * @date 2023/7/21 00:39
     * @param loginParam 登录参数
     * @return org.wm.commons.response.ResponseResult<?>
     */
    @PostMapping("/token")
    public ResponseResult<?> token(@RequestBody LoginParam loginParam) {
        var token = authService.login(loginParam.getUsername(), loginParam.getPassword(), loginParam.getCode(), loginParam.getUuid());
        return ResponseResult.success("登录成功", token);
    }

    /**
     * 功能描述：<功能描述>
     *       获取登录验证码
     * @author dove
     * @date 2023/7/24 23:20
     * @return org.wm.commons.response.ResponseResult<?>
     */
    @GetMapping("/captcha")
    public ResponseResult<?> captcha() {

        // var map = authService.captcha();

        return ResponseResult.success("hello captcha");
    }



    @GetMapping("/hello")
    public ResponseResult<?> testHello() {
        return demoServiceClient.helloDemo();
    }

    @GetMapping("/userinfo")
    public Map<String, Object> userInfo() {

        return null;
    }

    @GetMapping("/menu/{menuId}")
    public ResponseResult<?> menuInfo(@PathVariable Long menuId) {
        ResponseResult<?> menuInfo = menuServiceClient.getMenuInfo(menuId);
        return menuInfo;
    }


    @GetMapping("/user/{username}")
    public ResponseResult<?> userInfo(@PathVariable String username) {
        var user = userServiceClient.userInfoByUsername(username);
        return ResponseResult.success(user);
    }

}
