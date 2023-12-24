package org.wm.auth.code.captcha.context;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wm.auth.code.captcha.enums.CaptchaType;
import org.wm.auth.code.captcha.processor.CaptchaProcessor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/10/18 23:45
 * @since 1.0
**/
@Data
@Component
@RequiredArgsConstructor
public class CaptchaStrategyFactory implements InitializingBean {


    private final List<CaptchaProcessor> captchaProcessors;


    private Map<CaptchaType, CaptchaProcessor> map = new ConcurrentHashMap<>();


    public CaptchaProcessor getStrategy(CaptchaType type) {
        return map.get(type);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        map = captchaProcessors.stream().collect(Collectors.toMap(CaptchaProcessor::getKey, v -> v));
    }
}
