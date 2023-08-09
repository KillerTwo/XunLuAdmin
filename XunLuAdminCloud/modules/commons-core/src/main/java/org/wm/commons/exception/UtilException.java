package org.wm.commons.exception;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     工具类异常
 * @author dove
 * @date 2023/7/19 21:58
 * @since 1.0
 **/
public class UtilException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
