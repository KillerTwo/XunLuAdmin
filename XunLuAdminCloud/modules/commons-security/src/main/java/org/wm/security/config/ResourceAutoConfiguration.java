package org.wm.security.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.wm.commons.constants.ServiceNameConstants;
import org.wm.commons.utils.StringUtils;
import org.wm.commons.web.utils.ServletUtils;
import org.wm.security.authentication.jwt.CustomAuthenticationConverter;
import org.wm.security.authentication.opequeToken.CustomOpaqueTokenAuthenticationConverter;
import org.wm.security.authentication.opequeToken.UserInfoOpaqueTokenIntrospector;
import org.wm.security.authentication.settings.OAuth2TokenType;
import org.wm.security.config.properties.PermitAllUrlProperties;
import org.wm.security.constans.OAuth2TokenConstants;
import org.wm.security.utils.ResourceServerUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Random;

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
@RefreshScope
@Configuration
@EnableConfigurationProperties(PermitAllUrlProperties.class)
public class ResourceAutoConfiguration {


    private final DiscoveryClient discoveryClient;

    @Value(value = "${oauth2.client-id}")
    private String clientId = null;


    @Value("${oauth2.client-secret}")
    private String clientSecret = null;


    @Bean
    @ConditionalOnProperty({"oauth2.client-id", "oauth2.client-secret"})
    public OpaqueTokenIntrospector introspector() {

        var url = ResourceServerUtils.serverUrl(discoveryClient);

        var introspectionUri = StringUtils.format("{}/oauth2/introspect", url);

        return new UserInfoOpaqueTokenIntrospector(url, introspectionUri, clientId, clientSecret);
    }





    @Bean
    @Primary
    public JwtDecoder myJwtDecoder() {
        // String serveAddr = "http://192.168.1.3:8848";
        // var naming = NamingFactory.createNamingService(serveAddr);


        // var instances = naming.getAllInstances(ServiceNameConstants.OAUTH2_SERVICE);
        // var instances = discoveryClient.getInstances(ServiceNameConstants.OAUTH2_SERVICE);

        var issuer = ResourceServerUtils.serverUrl(discoveryClient);
        /*if (!instances.isEmpty()) {
            // var instance = instances.get(0);
            // 随机选择一个
            var instance = instances.get(new Random().nextInt(instances.size()));

            issuer = instance.getUri().toString();  // String.format("http://%s", instance.toInetAddr());
            log.info("issuer: {}", issuer);
        } else {
            log.error("找不到服务{}, {}", issuer, ServiceNameConstants.OAUTH2_SERVICE);
        }*/
        var jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuer);

        // 不验证issuer
        // OAuth2TokenValidator<Jwt> audienceValidator = audienceValidator();
        // OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);


        // 配置一个时间容忍差，在该时间范围内都认为token是有效的
        /*OAuth2TokenValidator<Jwt> withClockSkew = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ofSeconds(60)),
                new JwtIssuerValidator(issuerUri));

        jwtDecoder.setJwtValidator(withClockSkew);*/


        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(new JwtTimestampValidator());
        jwtDecoder.setJwtValidator(withAudience);


        /*MappedJwtClaimSetConverter converter = MappedJwtClaimSetConverter
                .withDefaults(Collections.singletonMap("sub", this::lookupUserIdBySub));

        jwtDecoder.setClaimSetConverter(converter);*/

        return jwtDecoder;
    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> tokenAuthenticationManagerResolver
            (OpaqueTokenIntrospector opaqueTokenIntrospector) {

        // jwt token
        var jwtAuthenticationProvider = new JwtAuthenticationProvider(myJwtDecoder());
        jwtAuthenticationProvider.setJwtAuthenticationConverter(customAuthenticationConverter());

        AuthenticationManager jwt = new ProviderManager(jwtAuthenticationProvider);
        // opaque token
        var provider = new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector);
        provider.setAuthenticationConverter(new CustomOpaqueTokenAuthenticationConverter());
        // provider.setAuthenticationConverter();
        AuthenticationManager opaqueToken = new ProviderManager(
                provider
                );

        return (request) -> useJwt(request) ? jwt : opaqueToken;
    }

    /**
     * 功能描述：<功能描述>
     * 判断jwt token 还是 opaque token
     *
     * @param request HttpServletRequest
     * @return boolean
     * @author dove
     * @date 2023/8/6 16:32
     */
    protected boolean useJwt(HttpServletRequest request) {
        var tokenType = ServletUtils.getHeader(request, OAuth2TokenConstants.TOKEN_TYPE_HEADER);
        return !OAuth2TokenType.REFERENCE.getValue().equals(tokenType);
    }


    /**
     * 功能描述：<功能描述>
     * 不用创建为Bean
     *
     * @return org.wm.security.authentication.jwt.CustomAuthenticationConverter
     * @author dove
     * @date 2023/8/5 21:16
     */
    // @Bean
    public CustomAuthenticationConverter customAuthenticationConverter() {
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        var jwtAuthenticationConverter = new CustomAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
