package org.wm.authentication.sms.generator;

/**
 *  验证码生成器
 */
public interface CaptchaGenerator {

    /**
     *
     * @return 生成短信验证码
     */
    String generatorCaptcha();

}