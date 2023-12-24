package org.wm.auth.code.sms.processor;

import org.springframework.web.context.request.ServletWebRequest;

import java.util.TreeMap;

/**
 *  发送短信处理器
 */
public interface SmsProcessor {

    /**
     *  处理短信验证码发送
     * @param request  封装http请求和响应
     * @param mobiles 手机号数组
     * @param key 缓存Key
     * @param templateId 短信模版ID
     * @return
     */
    String dealWithSendCaptcha(ServletWebRequest request, String templateId, String key, String[] mobiles);

    /**
     *  处理短信验证码发送
     * @param request  封装http请求和响应
     * @param mobiles 手机号数组
     * @param key 缓存Key
     * @param templateId 短信模版ID
     * @param params 发送参数
     * @return
     */
    String dealWithSendCaptcha(ServletWebRequest request, String templateId, String key, String[] mobiles,
                               TreeMap<String, Object> params);

    /**
     *  对比请求中传过来的验证码和前面生成的验证码是否一致
     * @param request 封装http请求和响应
     * @param captcha 验证码
     * @param key 缓存Key
     * @return 验证码是否一致
     */
    boolean validateCaptcha(ServletWebRequest request, String key, String captcha);

}
