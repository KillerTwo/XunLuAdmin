package org.wm.generator.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

/**
 * 功能描述：<功能描述>
 * 资源服务器配置
 *
 * @author dove
 * @date 2023/08/02 22:19
 * @since 1.0
 **/
@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ResourceServerConfig {

    private final AuthenticationEntryPointImpl unauthorizedHandler;


    private final AuthenticationManagerResolver<HttpServletRequest> tokenAuthenticationManagerResolver;

    private static final String[] DEFAULT_IGNORE_URLS = new String[] { "/actuator/**", "/error", "/v3/api-docs", "/webjars/**" };

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*http
                .securityMatcher("/messages/**")
                .authorizeHttpRequests()
                .requestMatchers("/messages/**").hasAuthority("SCOPE_message.read")
                .and()
                .oauth2ResourceServer()
                .jwt();*/



        AntPathRequestMatcher[] requestMatchers = Arrays.stream(DEFAULT_IGNORE_URLS)
                .map(AntPathRequestMatcher::new)
                .toList()
                .toArray(new AntPathRequestMatcher[]{});

        http
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                // .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // 基于token，所以不需要session
                // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeHttpRequests()
                // 对于登录login 注册register 验证码captchaImage 发送短信验证码captcha 重置密码resetPassword 允许匿名访问
                .requestMatchers(requestMatchers).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                /*.oauth2ResourceServer(
                        oauth2 -> oauth2.opaqueToken(token -> token.introspector(nimbusOpaqueTokenIntrospector))
                                .authenticationEntryPoint(unauthorizedHandler)
                                .bearerTokenResolver(bearerTokenResolver));*/
                .oauth2ResourceServer((oauth2) -> oauth2
                                .authenticationManagerResolver(tokenAuthenticationManagerResolver)
                                .authenticationEntryPoint(unauthorizedHandler)

                        // 统一异常处理器中处理SecurityExceptionHandler
                        // .accessDeniedHandler(customAccessDeniedHandler)

                );

        return http.build();
    }



}
