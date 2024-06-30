package org.wm.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/validator")
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

    /**
     * 功能描述：<功能描述>
     *       验证验证码是否正确
     * @author dove
     * @date 2024/6/27 23:11
     * @param code  验证码
     * @param uuid  uuid
     * @param request
     * @param response
     * @return org.wm.commons.response.ResponseResult<?>
     * @throws
     */
    @GetMapping("/validateCaptcha/{uuid}/{code}")
    public ResponseResult<?> validateCaptcha(@PathVariable String code, @PathVariable String uuid,
                                             HttpServletRequest request, @Nullable HttpServletResponse response) {

        var success = validatorService.validateCaptcha(code, uuid, request, response);

        return ResponseResult.success(success);
    }

}
