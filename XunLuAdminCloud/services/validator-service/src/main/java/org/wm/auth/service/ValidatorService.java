package org.wm.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * 功能描述：<功能描述>
 * 验证码Service
 * @author dove 
 * @date 2024/01/22 00:20
 * @since 1.0
**/
public interface ValidatorService {


    /**
     * 功能描述：<功能描述>
     *       获取登录验证码
     * @author dove
     * @date 2023/7/24 23:19
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> captcha(HttpServletRequest request, @Nullable HttpServletResponse response);


    /**
     * 功能描述：<功能描述>
     *       校验验证码
     * @author dove
     * @date 2024/1/22 00:24
     * @param username 用户名
     * @param code 验证码
     * @param uuid 验证码uuid
     * @return void
     * @throws
     */
    void validateCaptcha(String username, String code, String uuid);

}
