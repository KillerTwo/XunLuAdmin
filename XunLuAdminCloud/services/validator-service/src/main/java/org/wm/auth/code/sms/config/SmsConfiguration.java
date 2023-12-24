package org.wm.auth.code.sms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wm.auth.code.sms.SmsProperty;
import org.wm.auth.code.sms.processor.CodeSmsSendTemplate;
import org.wm.auth.code.sms.sendor.AliyunSmsSender;
import org.wm.auth.code.sms.sendor.TencentSmsSender;
import org.wm.auth.code.cache.HttpSessionSmsCache;
import org.wm.auth.code.cache.SmsCache;
import org.wm.auth.code.generator.CaptchaGenerator;
import org.wm.auth.code.generator.DefaultCaptchaGenerator;


@RequiredArgsConstructor
@Configuration
public class SmsConfiguration {


    private final AliyunSmsSender aliyunSmsSender;
    private final TencentSmsSender tencentSmsSender;

    private final SmsProperty smsProperty;


    @Bean
    public CodeSmsSendTemplate tencentCodeProcessor() {
        return new CodeSmsSendTemplate(codeCaptchaGenerator(), httpSessionCache(), tencentSmsSender);
    }

    @Bean
    public CodeSmsSendTemplate aliyunCodeProcessor() {
        return new CodeSmsSendTemplate(codeCaptchaGenerator(), httpSessionCache(), aliyunSmsSender);
    }

    @Bean
    public CaptchaGenerator codeCaptchaGenerator() {
        return new DefaultCaptchaGenerator(smsProperty);
    }

    @Bean
    public SmsCache httpSessionCache() {
        return new HttpSessionSmsCache();
    }

}
