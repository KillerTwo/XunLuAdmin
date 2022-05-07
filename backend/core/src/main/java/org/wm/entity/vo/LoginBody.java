package org.wm.entity.vo;

import lombok.Data;

/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 用户登录对象
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid = "";

    /**
     *  自动登录
     */
    private Boolean autoLogin = false;

    /**
     *  登录方式
     */
    private String type = "account";

    /**
     *  手机验证码
     */
    private String phoneCode;
}
