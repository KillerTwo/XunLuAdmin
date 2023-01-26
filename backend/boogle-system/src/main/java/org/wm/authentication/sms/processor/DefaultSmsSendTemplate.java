package org.wm.authentication.sms.processor;

import org.wm.authentication.sms.cache.SmsCache;
import org.wm.authentication.sms.generator.CaptchaGenerator;
import org.wm.authentication.sms.sendor.SmsSender;


// @Component
public class DefaultSmsSendTemplate extends CodeSmsSendTemplate {

    public DefaultSmsSendTemplate(CaptchaGenerator captchaGenerator, SmsCache smsCache, SmsSender smsSender) {
        super(captchaGenerator, smsCache, smsSender);
    }


}
