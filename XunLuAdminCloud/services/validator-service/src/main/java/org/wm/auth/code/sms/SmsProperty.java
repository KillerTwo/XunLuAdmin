package org.wm.auth.code.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *  短信相关配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProperty {

    /**
     *  短信地址
     */
    private String url = "";

    /**
     *  用户名
     */
    private String username = "";

    /**
     *  密码
     */
    private String password = "";

    /**
     *  存储验证码的键名称
     */
    private String captchaSessionName = "captcha";

    /**
     *  验证码位数
     */
    private Integer captchaNumber = 4;


    private String encoding = "UTF-8";

    /**
     *  短信接口类型
     */
    private SmsInterfaceType smsInterface = SmsInterfaceType.ALICLOUD;


    private String signName = "";

    /**
     * 密钥对 SecretId
     */
    private String secretId;

    /**
     *  密钥对 SecretKey
     */
    private String secretKey;

    /**
     *  短信SdkAppId
     */
    private String sdkAppId;

}
