package org.wm.auth.service.impl;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import org.springframework.web.context.request.ServletWebRequest;
import org.wm.auth.code.captcha.enums.CaptchaType;
import org.wm.auth.code.captcha.facade.CaptchaFacade;

import org.wm.auth.service.ValidatorService;
import org.wm.commons.constants.Constants;
import org.wm.commons.constants.RedisKeyConstants;
import org.wm.commons.exception.user.CaptchaExpireException;
import org.wm.commons.utils.StringUtils;

import org.wm.redis.service.RedisCache;



import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：<功能描述>
 * 验证器Service
 * @author dove 
 * @date 2024/01/22 00:20
 * @since 1.0
**/
@RefreshScope
@Slf4j
@RequiredArgsConstructor
@Service
public class ValidatorServiceImpl implements ValidatorService {

    private final RedisCache redisCache;

    private final CaptchaFacade captchaFacade;

    @Value("${captcha.captcha-type}")
    private String captchaType;

    @Override
    public Map<String, Object> captcha(HttpServletRequest request, @Nullable HttpServletResponse response) {
        boolean captchaOnOff = redisCache.getCacheObject(RedisKeyConstants.CAPTCHA_ON_OFF);
        Map<String, Object> map = new HashMap<>();
        map.put("captchaOnOff", captchaOnOff);
        if (!captchaOnOff) {
            return map;
        }

        var res = captchaFacade.obtainCodeBase64(CaptchaType.getCaptchaType(captchaType),
                new ServletWebRequest(request, response), null);

        map.put("code", res.getCode());
        map.put("uuid", res.getKey());
        map.put("img", res.getImageBase64());
        return map;
    }


    /**
     * 校验验证码
     *
     * @param code     验证码
     * @param uuid     唯一标识
     */
    @Override
    public boolean validateCaptcha(String code, String uuid,
                                HttpServletRequest request, @Nullable HttpServletResponse response) {
        // String verifyKey = Constants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        // String captcha = redisCache.getCacheObject(verifyKey);
        // redisCache.deleteObject(verifyKey);

        var success = captchaFacade.validateCode(CaptchaType.getCaptchaType(captchaType),
                new ServletWebRequest(request, response), uuid, code);

        if (!success) {
            // TODO 需要重新设置调用记录日志的方式：服务调用或者消息中间件的方式
            // AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            return false;
        }
        return true;
    }

}
