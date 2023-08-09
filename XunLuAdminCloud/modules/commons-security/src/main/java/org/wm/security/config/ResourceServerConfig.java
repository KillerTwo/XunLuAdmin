package org.wm.security.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.wm.security.authentication.jwt.CustomAuthenticationConverter;
import org.wm.security.config.properties.PermitAllUrlProperties;
import org.wm.security.handler.AuthenticationEntryPointImpl;

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

    private final PermitAllUrlProperties permitAllUrl;

    private final AuthenticationManagerResolver<HttpServletRequest> tokenAuthenticationManagerResolver;


    // private final OpaqueTokenIntrospector nimbusOpaqueTokenIntrospector = new NimbusOpaqueTokenIntrospector("", "", "");

    // private final BearerTokenResolver bearerTokenResolver;


    /**
     * 如果使用这种方式创建AuthenticationManager Bean，并且没有配置其他任何AuthenticationManager的属性，
     * ProviderManager的this.parent不为空，用户认证失败时可能会导致如下代码死循环
     * if (result == null && this.parent != null) {
     * // Allow the parent to try.
     * try {
     * parentResult = this.parent.authenticate(authentication);
     * result = parentResult;
     * }
     * catch (ProviderNotFoundException ex) {
     * // ignore as we will throw below if no other exception occurred prior to
     * // calling parent and the parent
     * // may throw ProviderNotFound even though a provider in the child already
     * // handled the request
     * }
     * catch (AuthenticationException ex) {
     * parentException = ex;
     * lastException = ex;
     * }* 		}
     */

    // @Bean
    // @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        // authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());   // 如果不配置，则默认的DaoAuthenticationProvider不会被配置
        // authenticationManagerBuilder.authenticationProvider(smsAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }


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

        AntPathRequestMatcher[] requestMatchers = permitAllUrl.getUrls()
                .stream()
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

        // .oauth2ResourceServer()
        // .jwt();

        // httpSecurity.authenticationProvider(smsAuthenticationProvider());
        return http.build();
    }



}
