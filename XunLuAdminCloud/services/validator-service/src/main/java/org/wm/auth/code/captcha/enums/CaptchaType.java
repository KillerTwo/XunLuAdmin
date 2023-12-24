package org.wm.auth.code.captcha.enums;

/**
 * 功能描述：<功能描述>
 *     验证码类型
 *
 * @author dove 
 * @date 2023/10/18 23:46
 * @since 1.0
**/
public enum CaptchaType {
    MATH("math", "数字计算验证码"),
    CHAR("char", "字符验证码");


    private String code;


    private String desc;

    CaptchaType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static CaptchaType getCaptchaType(String type) {
        for (CaptchaType value : CaptchaType.values()) {
            if (value.getCode().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
