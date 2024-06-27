package org.wm.auth.code.captcha.processor.impl;

import org.springframework.web.context.request.ServletWebRequest;
import org.wm.auth.code.captcha.processor.CaptchaProcessor;
import org.wm.redis.service.RedisCache;

/**
 * 功能描述：<功能描述>
 * 验证码处理器抽象器类
 * @author dove 
 * @date 2024/01/23 00:11
 * @since 1.0
**/
public abstract class AbstractCaptchaProcessor implements CaptchaProcessor {

    @Override
    public boolean validateCaptcha(ServletWebRequest request, String key, String captcha) {
        var redisCache = getCacheObject();
        String code = redisCache.getCacheObject(key);
        redisCache.deleteObject(key);
        if (captcha == null) {
            // TODO 需要重新设置调用记录日志的方式：服务调用或者消息中间件的方式
            // AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            // throw new CaptchaExpireException();
            return false;
        }
        if (code == null || !code.equalsIgnoreCase(captcha)) {
            // AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            // throw new CaptchaException();
            return false;
        }

        return true;
    }

    protected abstract RedisCache getCacheObject();

}
