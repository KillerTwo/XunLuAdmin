package org.wm.authentication.sms.cache;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;

@Component
public class HttpSessionSmsCache implements SmsCache {

    @Override
    public void cacheCaptcha(ServletWebRequest request, String key, String captcha) {
        HttpSession session = request.getRequest().getSession();

        session.setAttribute(key, captcha);
    }

    @Override
    public boolean validateCaptcha(ServletWebRequest request, String key, String captcha) {
        HttpSession session = request.getRequest().getSession();
        String sessionCaptcha = (String) session.getAttribute(key);

        if(StringUtils.isEmpty(captcha) || StringUtils.isEmpty(sessionCaptcha)) {
            return false;
        }

        if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(captcha, sessionCaptcha)) {
            return true;
        }
        return false;
    }

    @Override
    public void removeCache(ServletWebRequest request, String key) {
        HttpSession session = request.getRequest().getSession();
        session.removeAttribute(key);
    }
}
