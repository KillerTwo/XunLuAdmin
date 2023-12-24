package org.wm.auth.code.captcha.context;

import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.ServletWebRequest;
import org.wm.auth.code.captcha.CaptchaRes;
import org.wm.auth.code.captcha.processor.CaptchaProcessor;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/10/18 23:56
 * @since 1.0
**/

@RequiredArgsConstructor
public class CaptchaStrategyContext {

    private final CaptchaProcessor captchaProcessor;

    /**
     * 功能描述：<功能描述>
     *       获取验证码
     * @author dove
     * @date 2023/10/19 00:05
     * @param request  ServletWebRequest
     * @param key  验证码缓存key
     * @return CaptchaRes
     */
    public CaptchaRes obtainCodeBase64(ServletWebRequest request, String key) {
        return captchaProcessor.dealWithGenerateCaptcha(request, key);
    }

    /**
     * 功能描述：<功能描述>
     *       验证验证码是否和缓存中一致
     * @author dove
     * @date 2023/10/19 00:05
     * @param request  ServletWebRequest
     * @param key 验证码缓存key
     * @param code  验证码
     * @return boolean true/false
     */
    public boolean validate(ServletWebRequest request, String key, String code) {
        return captchaProcessor.validateCaptcha(request, key, code);
    }
}
