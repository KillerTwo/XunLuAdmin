package org.wm.commons.exception.user;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     验证码失效异常类
 * @author dove
 * @date 2023/7/19 21:56
 * @since 1.0
 **/
public class CaptchaExpireException extends UserException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}
