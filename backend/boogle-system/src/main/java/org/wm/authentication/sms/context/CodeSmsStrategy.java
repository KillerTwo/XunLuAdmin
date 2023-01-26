package org.wm.authentication.sms.context;

import org.wm.authentication.sms.SmsProperty;
import org.wm.authentication.sms.generator.CaptchaGenerator;
import org.wm.authentication.sms.processor.DefaultSmsSendTemplate;
import org.wm.authentication.sms.processor.SmsProcessor;
import org.wm.authentication.sms.sendor.AliyunSmsSender;
import org.wm.authentication.sms.sendor.TencentSmsSender;
import org.wm.utils.SpringUtils;

public enum CodeSmsStrategy {


    // 阿里云短信接口
    ALI_YUN("aliyun", "阿里云短信接口") {
        @Override
        public SmsProcessor smsProcess() {
            return SpringUtils.getBean("aliyunCodeProcessor");
        }
    },

    // 腾讯短信接口
    TEN_CENT("tencent", "腾讯短信接口") {
        @Override
        public SmsProcessor smsProcess() {
            return SpringUtils.getBean("tencentCodeProcessor");
        }
    };

    private String value;

    private String desc;

    public abstract SmsProcessor smsProcess();

    CodeSmsStrategy(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
