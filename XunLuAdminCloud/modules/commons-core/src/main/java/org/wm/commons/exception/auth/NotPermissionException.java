package org.wm.commons.exception.auth;

import org.apache.commons.lang3.StringUtils;

/**
 * 功能描述：<功能描述>
 * 未能通过的权限认证异常
 *
 * @author dove
 * @date 2023/7/12 23:41
 * @since 1.0
 **/
public class NotPermissionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotPermissionException(String permission) {
        super(permission);
    }

    public NotPermissionException(String[] permissions) {
        super(StringUtils.join(permissions, ","));
    }
}
