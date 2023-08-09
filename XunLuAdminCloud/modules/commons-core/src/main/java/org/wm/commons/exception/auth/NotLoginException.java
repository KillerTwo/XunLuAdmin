package org.wm.commons.exception.auth;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     未能通过的登录认证异常
 *
 * @author dove
 * @date 2023/7/12 23:41
 * @since 1.0
 **/
public class NotLoginException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotLoginException(String message) {
        super(message);
    }
}
