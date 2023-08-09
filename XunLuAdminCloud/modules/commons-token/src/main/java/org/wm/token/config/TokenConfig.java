package org.wm.token.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.wm.token.jks.JKSPathProperties;
import org.wm.token.jks.RSAGenerator;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/07/20 01:09
 * @since 1.0
**/
@RequiredArgsConstructor
@Configuration
public class TokenConfig {

    private final JKSPathProperties jksPathProperties;


    @Lazy()
    @Bean
    public RSAGenerator rsaGenerator() {
        return RSAGenerator.getInstance(jksPathProperties.getPath(),
                jksPathProperties.getAlias(), jksPathProperties.getPassword());
    }

}
