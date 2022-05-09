package org.wm.entity.vo;

import lombok.Data;

/**
 * @创建人 sk
 * @创建时间 2022/4/9
 * @描述
 */
@Data
public class ResetPasswordBody {

    /**
     *  手机号
     */
    private String phone;

    /**
     *  用户名
     */
    private String username;

    /**
     *  验证码
     */
    private String code;

    /**
     *  验证码uuid
     */
    private String uuid;

    /**
     *  短信验证码
     */
    private String phoneCode;

    /**
     *  新密码
     */
    private String password;

    /**
     *  验证密码
     */
    private String rePassword;

}
