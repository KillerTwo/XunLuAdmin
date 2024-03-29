package org.wm.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wm.auth.service.ValidatorService;
import org.wm.commons.response.ResponseResult;

/**
 * 功能描述：<功能描述>
 * 验证器API 获取短信验证码、图片验证码、验证验证码
 * @author dove 
 * @date 2024/01/22 00:18
 * @since 1.0
**/
@RequiredArgsConstructor
@RestController
public class ValidatorController {


    private final ValidatorService validatorService;


    /**
     * 功能描述：<功能描述>
     *       获取登录验证码
     * @author dove
     * @date 2023/7/24 23:20
     * @return org.wm.commons.response.ResponseResult<?>
     */
    @GetMapping("/captchaImage")
    public ResponseResult<?> captcha(HttpServletRequest request, @Nullable HttpServletResponse response) {

        var map = validatorService.captcha(request, response);

        return ResponseResult.success(map);
    }

}
