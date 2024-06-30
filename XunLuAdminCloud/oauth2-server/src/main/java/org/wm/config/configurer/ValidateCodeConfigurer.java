package org.wm.config.configurer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.wm.authentication.filter.ValidateCodeFilter;

/**
 * 功能描述：<功能描述>
 * 配置验证码过滤器链
 * @author dove 
 * @date 2024/06/30 17:51
 * @since 1.0
**/
@RequiredArgsConstructor
@Component
public class ValidateCodeConfigurer extends AbstractHttpConfigurer<ValidateCodeConfigurer, HttpSecurity> {

    private final ValidateCodeFilter validateCodeFilter;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        builder.addFilterBefore(validateCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }
}
