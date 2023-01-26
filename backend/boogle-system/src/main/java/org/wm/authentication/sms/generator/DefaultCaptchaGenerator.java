package org.wm.authentication.sms.generator;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wm.authentication.sms.SmsProperty;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class DefaultCaptchaGenerator implements CaptchaGenerator {

    private final SmsProperty smsProperty;

    @Override
    public String generatorCaptcha() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.char.length", smsProperty.getCaptchaNumber().toString());
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.textproducer.char.space", "4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha.createText();
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.char.length", "12");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.textproducer.char.space", "4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        System.err.println(defaultKaptcha.createText());
    }

}
