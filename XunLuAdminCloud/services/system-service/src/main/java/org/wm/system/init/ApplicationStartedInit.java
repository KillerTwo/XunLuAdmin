package org.wm.system.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.wm.commons.constants.RedisKeyConstants;
import org.wm.redis.service.RedisCache;
import org.wm.system.service.ISysConfigService;

/**
 * 功能描述：<功能描述>
 *     Spring boot应用启动完成后执行自定义初始化
 *
 * @author dove 
 * @date 2023/07/19 23:31
 * @since 1.0
**/
@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationStartedInit implements ApplicationRunner {

    private final RedisCache redisCache;

    private final ISysConfigService configService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 缓存登录是否需要验证码参数
        var captchaOnOff = configService.selectCaptchaOnOff();
        redisCache.setCacheObject(RedisKeyConstants.CAPTCHA_ON_OFF, captchaOnOff);
    }
}
