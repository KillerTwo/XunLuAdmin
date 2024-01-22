package org.wm.auth.code.captcha.processor.impl;

import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.wm.auth.code.captcha.CaptchaRes;
import org.wm.auth.code.captcha.enums.CaptchaType;
import org.wm.auth.code.captcha.processor.CaptchaProcessor;
import org.wm.auth.enums.CaptchaGeneratorEnum;
import org.wm.commons.constants.Constants;
import org.wm.commons.exception.user.CaptchaException;
import org.wm.commons.exception.user.CaptchaExpireException;
import org.wm.redis.service.RedisCache;

import java.util.concurrent.TimeUnit;

/**
 * 功能描述：<功能描述>
 * 数字验证码处理器
 * @author dove 
 * @date 2024/01/22 23:31
 * @since 1.0
**/
@RequiredArgsConstructor
@Component
public class MathCaptchaProcessor extends AbstractCaptchaProcessor {

    private final RedisCache redisCache;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;


    @Override
    public CaptchaRes dealWithGenerateCaptcha(ServletWebRequest request, String key) {
        var res = CaptchaGeneratorEnum.MATH_CAPTCHA.generate(captchaProducerMath);

        redisCache.setCacheObject(res.getKey(), res.getCode(), Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        return res;
    }

    @Override
    protected RedisCache getCacheObject() {
        return redisCache;
    }

    @Override
    public CaptchaType getKey() {
        return CaptchaType.MATH;
    }
}
