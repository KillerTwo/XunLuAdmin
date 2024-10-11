package org.wm.generator.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;

import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.wm.generator.util.StringUtils;


/**
 * 功能描述：<功能描述>
 *     相关bean自动配置类
 *
 * @author dove 
 * @date 2023/08/06 16:37
 * @since 1.0
**/
@RequiredArgsConstructor
@Slf4j
@Configuration
public class ResourceAutoConfiguration {



    // @Value(value = "${oauth2.client-id}")
    private String clientId = "messaging-client";


    // @Value("${oauth2.client-secret}")
    private String clientSecret = "password";


    @Bean
    public OpaqueTokenIntrospector introspector() {
        var url = "http://localhost:8080";

        var introspectionUri = StringUtils.format("{}/oauth2/introspect", url);

        return new UserInfoOpaqueTokenIntrospector(url, introspectionUri, clientId, clientSecret);
    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> tokenAuthenticationManagerResolver
            (OpaqueTokenIntrospector opaqueTokenIntrospector) {
        // opaque token
        var provider = new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector);
        provider.setAuthenticationConverter(new CustomOpaqueTokenAuthenticationConverter());
        // provider.setAuthenticationConverter();
        AuthenticationManager opaqueToken = new ProviderManager(
                provider
                );

        return (request) -> opaqueToken;
    }

}
