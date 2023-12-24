package org.wm.auth.code.captcha.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.wm.auth.code.captcha.CaptchaRes;
import org.wm.auth.code.captcha.context.CaptchaStrategyContext;
import org.wm.auth.code.captcha.context.CaptchaStrategyFactory;
import org.wm.auth.code.captcha.enums.CaptchaType;

/**
 * 功能描述：<功能描述>
 *     门面模式
 *
 * @author dove 
 * @date 2023/10/19 00:07
 * @since 1.0
**/
@Component
@RequiredArgsConstructor
public class CaptchaFacade {

    private final CaptchaStrategyFactory captchaStrategyFactory;

    /**
     * 功能描述：<功能描述>
     *       获取验证码
     * @author dove
     * @date 2023/10/19 00:33
     * @param type  验证码类型
     * @param request  ServletWebRequest
     * @param key 验证码缓存key
     * @return CaptchaRes
     */
    public CaptchaRes obtainCodeBase64(CaptchaType type, ServletWebRequest request, String key) {
        var context = new CaptchaStrategyContext(captchaStrategyFactory.getStrategy(type));
        return context.obtainCodeBase64(request, key);
    }

    /**
     * 功能描述：<功能描述>
     *       验证验证码是否和缓存中一致
     * @author dove
     * @date 2023/10/19 00:34
     * @param type 验证码类型
     * @param request ServletWebRequest
     * @param key  验证码缓存key
     * @param code  验证码
     * @return boolean true/false
     */
    public boolean validateCode(CaptchaType type, ServletWebRequest request, String key, String code) {
        var context = new CaptchaStrategyContext(captchaStrategyFactory.getStrategy(type));
        return context.validate(request, key, code);
    }


}
