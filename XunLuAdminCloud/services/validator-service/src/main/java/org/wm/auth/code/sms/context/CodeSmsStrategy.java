package org.wm.auth.code.sms.context;


import org.wm.auth.code.sms.processor.SmsProcessor;
import org.wm.commons.utils.SpringContextHolder;

public enum CodeSmsStrategy {


    // 阿里云短信接口
    ALI_YUN("aliyun", "阿里云短信接口") {
        @Override
        public SmsProcessor smsProcess() {
            return SpringContextHolder.getBean("aliyunCodeProcessor");
        }
    },

    // 腾讯短信接口
    TEN_CENT("tencent", "腾讯短信接口") {
        @Override
        public SmsProcessor smsProcess() {
            return SpringContextHolder.getBean("tencentCodeProcessor");
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
