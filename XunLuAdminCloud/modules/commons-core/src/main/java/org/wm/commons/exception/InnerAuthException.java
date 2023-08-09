package org.wm.commons.exception;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     内部认证异常
 *
 * @author dove
 * @date 2023/7/12 23:32
 * @since 1.0
 **/
public class InnerAuthException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InnerAuthException(String message) {
        super(message);
    }
}
