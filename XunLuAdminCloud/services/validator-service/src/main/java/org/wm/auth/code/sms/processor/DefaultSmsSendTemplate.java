package org.wm.auth.code.sms.processor;


import org.wm.auth.code.cache.SmsCache;
import org.wm.auth.code.generator.CaptchaGenerator;
import org.wm.auth.code.sms.sendor.SmsSender;

// @Component
public class DefaultSmsSendTemplate extends CodeSmsSendTemplate {

    public DefaultSmsSendTemplate(CaptchaGenerator captchaGenerator, SmsCache smsCache, SmsSender smsSender) {
        super(captchaGenerator, smsCache, smsSender);
    }


}
