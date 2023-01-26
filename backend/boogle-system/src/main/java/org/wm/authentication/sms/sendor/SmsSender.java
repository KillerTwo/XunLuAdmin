package org.wm.authentication.sms.sendor;

import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 *  短信发送器接口
 */
public interface SmsSender {

    /**
     *  发送短信
     * @param mobiles 手机号数组
     * @param code 验证码
     * @return
     */
    String sender(ServletWebRequest request, String smsTemplate, String[] mobiles, String code);


    /**
     * 发送短信
     * @param request    ServletWebRequest
     * @param mobiles    手机号数组
     * @param param    参数
     * @return    String
     */
    String sender(ServletWebRequest request, String smsTemplate, String[] mobiles, TreeMap<String, Object> param);

}
