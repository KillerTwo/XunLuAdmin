package org.wm.authentication.sms.processor;

import org.springframework.web.context.request.ServletWebRequest;
import org.wm.authentication.sms.cache.SmsCache;
import org.wm.authentication.sms.generator.CaptchaGenerator;
import org.wm.authentication.sms.handler.SmsSendCompleteHandler;
import org.wm.authentication.sms.sendor.SmsSender;

import java.util.Map;
import java.util.TreeMap;

public class CodeSmsSendTemplate implements SmsProcessor {

    private final static boolean CACHE_CODE = true;

    private SmsSender smsSender;

    private CaptchaGenerator captchaGenerator;

    private SmsCache smsCache;

    private SmsSendCompleteHandler smsSendCompleteHandler = (templateId, mobiles, params, result) -> {};

    /**
     *  是否缓存短信内容
     */
    private boolean cache = CACHE_CODE;


    public CodeSmsSendTemplate(CaptchaGenerator captchaGenerator, SmsCache smsCache, SmsSender smsSender) {
        this.smsSender = smsSender;
        this.captchaGenerator = captchaGenerator;
        this.smsCache = smsCache;
    }

    @Override
    public String dealWithSendCaptcha(ServletWebRequest request, String templateId, String key, String[] mobiles) {
        String captcha = generateCaptcha(request);
        if (cache) {
            cacheCaptcha(request, key, captcha);
        }

        var result = smsSender.sender(request, templateId, mobiles, captcha);
        complete(templateId, mobiles, Map.of("code", captcha), result);
        return result;
    }

    @Override
    public String dealWithSendCaptcha(ServletWebRequest request, String templateId, String key, String[] mobiles,
                                      TreeMap<String, Object> params) {
        return null;
    }

    @Override
    public boolean validateCaptcha(ServletWebRequest request, String key, String captcha) {
        return smsCache.validateCaptcha(request, key, captcha);
    }

    /**
     *  缓存验证码
     * @param request 封装http请求和响应
     * @param captcha 验证码
     */
    protected void cacheCaptcha(ServletWebRequest request, String key, String captcha) {
        smsCache.cacheCaptcha(request, key, captcha);
    }


    /**
     *  生成验证码
     * @param request 封装http请求和响应
     * @return String
     */
    protected String generateCaptcha(ServletWebRequest request) {
        return captchaGenerator.generatorCaptcha();
    }

    /**
     *  短信发送完成后的处理逻辑
     * @param templateId 短信模版ID
     * @param mobiles 手机号
     * @param params 发送参数
     * @param result 发送结果
     */
    protected void complete(String templateId, String[] mobiles,
                            Map<String, Object> params, String result) {
        smsSendCompleteHandler.complete(templateId, mobiles, params, result);
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public void setSmsSendCompleteHandler(SmsSendCompleteHandler smsSendCompleteHandler) {
        this.smsSendCompleteHandler = smsSendCompleteHandler;
    }
}
