package org.wm.authentication.sms;

public enum SmsInterfaceType {

    /**
     * 阿里云短信接口
     */
    ALICLOUD("aLiCloud"),

    /**
     * 腾讯云短信接口
     */
    TENCENT("TENCENT");

    private String type;

    SmsInterfaceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
