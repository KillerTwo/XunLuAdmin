package org.wm.auth.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.wm.commons.constants.RedisKeyConstants;
import org.wm.redis.service.RedisCache;

/**
 * 功能描述：<功能描述>
 * Spring boot应用启动完成后执行自定义初始化
 * @author dove 
 * @date 2024/06/27 22:54
 * @since 1.0
**/
@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationStartedInit implements ApplicationRunner {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationStartedInit.class);

    private final RedisCache redisCache;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        try {
            // 如果没有设置过值，则会报错，重新设置值（单独运行验证服务时会使用到）
            var captchaOnOff = redisCache.getCacheObject(RedisKeyConstants.CAPTCHA_ON_OFF);
            if (captchaOnOff == null) {
                redisCache.setCacheObject(RedisKeyConstants.CAPTCHA_ON_OFF, true);
            }
            logger.info("{}", captchaOnOff);
        } catch (Exception e) {
            logger.warn("Application started failed", e);
            // 缓存登录是否需要验证码参数
            redisCache.setCacheObject(RedisKeyConstants.CAPTCHA_ON_OFF, true);
        }
    }

}
