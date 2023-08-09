package org.wm.commons.exception.user;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     用户密码不正确或不符合规范异常类
 * @author dove
 * @date 2023/7/19 21:57
 * @since 1.0
 **/
public class UserPasswordNotMatchException extends UserException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}
