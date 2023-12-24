package org.wm.auth.code.captcha;

import lombok.Data;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/10/19 00:37
 * @since 1.0
**/
@Data
public class CaptchaRes {

    /**
     *  验证码
     */
    private String code;

    /**
     *  验证码生成图片 base64
     */
    private String imageBase64;

    /**
     *  缓存key
     */
    private String key;

}
