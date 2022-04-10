package org.wm.entity.vo;

import lombok.Data;

/**
 * @创建人 sk
 * @创建时间 2022/4/9
 * @描述
 */
@Data
public class PhoneCodeBody {
    /**
     *  手机号
     */
    private String phone;

    /**
     *  验证码
     */
    private String code;

    /**
     *  验证码uuid
     */
    private String uuid;
}
