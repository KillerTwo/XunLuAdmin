package org.wm.commons.exception.user;


import org.wm.commons.exception.base.BaseException;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     用户信息异常类
 * @author dove
 * @date 2023/7/19 21:57
 * @since 1.0
 **/
public class UserException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
