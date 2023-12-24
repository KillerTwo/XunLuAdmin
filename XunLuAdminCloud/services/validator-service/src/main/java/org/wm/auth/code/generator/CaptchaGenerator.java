package org.wm.auth.code.generator;

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