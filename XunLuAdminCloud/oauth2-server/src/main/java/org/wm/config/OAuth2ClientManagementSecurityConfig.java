package org.wm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.wm.authentication.handler.AuthenticationEntryPointImpl;

/**
 * OAuth2客户端管理API的安全配置
 * 配置为资源服务器，要求JWT Bearer Token认证
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class OAuth2ClientManagementSecurityConfig {

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;

    /**
     * OAuth2客户端管理端点的安全配置
     * 配置优先级（数字越小优先级越高）：
     * - HIGHEST_PRECEDENCE (AuthorizationServerConfig) - OAuth2标准端点
     * - @Order(0) (DefaultSecurityConfig.resources) - 静态资源
     * - @Order(1) (本配置) - OAuth2客户端管理API
     * - 无@Order (DefaultSecurityConfig.defaultSecurityFilterChain) - 其他所有请求
     */
    @Bean
    @Order(1)
    public SecurityFilterChain oauth2ClientManagementSecurityFilterChain(
            HttpSecurity http,
            JWKSource<SecurityContext> jwkSource) throws Exception {

        http
                // 只匹配客户端管理相关的端点
                .securityMatcher("/oauth2/registeredClient/**")
                // 配置为资源服务器，使用JWT认证
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(
                                OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
                        ))
                        .authenticationEntryPoint(authenticationEntryPointImpl)
                )
                // 所有请求都需要认证
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                // 禁用CSRF（因为是API，使用JWT认证）
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
