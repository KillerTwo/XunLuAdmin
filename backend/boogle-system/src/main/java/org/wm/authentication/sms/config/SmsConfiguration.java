package org.wm.authentication.sms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wm.authentication.sms.SmsProperty;
import org.wm.authentication.sms.cache.HttpSessionSmsCache;
import org.wm.authentication.sms.cache.SmsCache;
import org.wm.authentication.sms.generator.CaptchaGenerator;
import org.wm.authentication.sms.generator.DefaultCaptchaGenerator;
import org.wm.authentication.sms.processor.CodeSmsSendTemplate;
import org.wm.authentication.sms.sendor.AliyunSmsSender;
import org.wm.authentication.sms.sendor.TencentSmsSender;

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
