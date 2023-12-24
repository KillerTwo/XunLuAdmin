package org.wm.auth.code.cache;

import org.springframework.web.context.request.ServletWebRequest;

public interface SmsCache {

    /**
     *  缓存验证码
     * @param request 封装http请求和响应
     * @param captcha 验证码
     * @param key 缓存key
     */
    void cacheCaptcha(ServletWebRequest request, String key, String captcha);

    /**
     *  对比请求中传过来的验证码和前面生成的验证码是否一致
     * @param request 封装http请求和响应
     * @param captcha 验证码
     * @param key 缓存key
     * @return 验证码是否一致
     */
    boolean validateCaptcha(ServletWebRequest request, String key, String captcha);


    /**
     *  清除缓存key
     * @param request 封装http请求和响应
     * @param key 缓存key
     */
    void removeCache(ServletWebRequest request, String key);

}
