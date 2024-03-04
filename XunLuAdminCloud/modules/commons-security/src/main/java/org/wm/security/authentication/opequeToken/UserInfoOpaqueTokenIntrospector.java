package org.wm.security.authentication.opequeToken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.web.client.RestOperations;
import org.wm.security.constans.OAuth2TokenConstants;

import java.security.PrivilegedAction;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.jwt.RegisteredPayload.EXPIRES_AT;
import static com.nimbusds.jwt.JWTClaimNames.ISSUED_AT;

/**
 * 功能描述：<功能描述>
 *     验证Opaque Token
 *
 * @author dove 
 * @date 2023/08/06 16:25
 * @since 1.0
**/
@Slf4j
public class UserInfoOpaqueTokenIntrospector extends NimbusOpaqueTokenIntrospector {


    public UserInfoOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        super(introspectionUri, clientId, clientSecret);
    }

    public UserInfoOpaqueTokenIntrospector(String introspectionUri, RestOperations restOperations) {
        super(introspectionUri, restOperations);
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        log.info("自定义Token验证逻辑：{}", token);
        var principal = super.introspect(token);

        Instant issuedAt = principal.getAttribute(ISSUED_AT);
        Instant expiresAt = principal.getAttribute(EXPIRES_AT);
        // 调用授权服务器的userinfo端点请求用户信息

        /*ClientRegistration clientRegistration = this.repository.findByRegistrationId("registration-id");
        OAuth2AccessToken token = new OAuth2AccessToken(BEARER, token, issuedAt, expiresAt);
        OAuth2UserRequest oauth2UserRequest = new OAuth2UserRequest(clientRegistration, token);
        return this.oauth2UserService.loadUser(oauth2UserRequest);*/


        return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(), principal.getAttributes(), extractAuthorities(principal));
    }


    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {

        // var authorities = principal.getAuthorities();
        var authorities = principal.getAttribute(OAuth2TokenConstants.AUTHORITIES_NAME);



        try {
            var objectMapper = new ObjectMapper();
            var s = objectMapper.writeValueAsString(authorities);

            var roles = objectMapper.readValue(s, new TypeReference<List<String>>() {
            });

            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("转换角色出错： authorities{}", authorities, e);
        }
        return Collections.emptyList();
    }
}
