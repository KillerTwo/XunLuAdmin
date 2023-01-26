package org.wm.authentication.sms;

public enum SmsInterfaceType {

    /**
     * 阿里云短信接口
     */
    ALICLOUD("aLiCloud"),

    /**
     * 云片网短信接口
     */
    CLOUDNETWORK("cloudNetwork");

    private String type;

    SmsInterfaceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
