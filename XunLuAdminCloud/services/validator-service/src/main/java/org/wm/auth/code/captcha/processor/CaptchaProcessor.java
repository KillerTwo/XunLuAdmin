package org.wm.auth.code.captcha.processor;

import org.springframework.web.context.request.ServletWebRequest;
import org.wm.auth.code.captcha.CaptchaRes;
import org.wm.auth.code.captcha.enums.CaptchaType;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/10/18 23:07
 * @since 1.0
**/
public interface CaptchaProcessor {


    /**
     * 功能描述：<功能描述>
     *       生成验证码
     * @author dove
     * @date 2023/10/18 23:08
     * @param request ServletWebRequest
     * @param key  captcha的缓存key
     * @return java.lang.String  验证码图片的base64字符串
     */
    CaptchaRes dealWithGenerateCaptcha(ServletWebRequest request, String key);



    /**
     * 功能描述：<功能描述>
     *       验证captcha是否是缓存中的验证码
     * @author dove
     * @date 2023/10/18 23:09
     * @param request ServletWebRequest
     * @param key  缓存key
     * @param captcha  待验证的验证码
     * @return boolean true/false
     */
    boolean validateCaptcha(ServletWebRequest request, String key, String captcha);


    /**
     * 功能描述：<功能描述>
     *       唯一标识
     * @author dove
     * @date 2023/10/18 23:52
     * @return org.wm.auth.code.captcha.enums.CaptchaType 返回唯一标识
     */
    CaptchaType getKey();

}
