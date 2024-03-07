package org.wm.security.authentication.opequeToken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.web.client.RestOperations;
import org.springframework.web.reactive.function.client.WebClient;
import org.wm.commons.dto.TransferDataMap;
import org.wm.commons.utils.StringUtils;
import org.wm.security.authentication.UserInfo;
import org.wm.security.constans.OAuth2TokenConstants;
import org.wm.security.utils.ResourceServerUtils;

import java.security.PrivilegedAction;
import java.time.Instant;
import java.util.*;
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
@Getter
@Slf4j
public class UserInfoOpaqueTokenIntrospector extends NimbusOpaqueTokenIntrospector {

    private String issuer;

    private final WebClient rest = WebClient.create();

    public UserInfoOpaqueTokenIntrospector(String issuer, String introspectionUri, String clientId, String clientSecret) {
        super(introspectionUri, clientId, clientSecret);
        this.issuer = issuer;
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

        var userinfoUri = StringUtils.format("{}/oauth2/userinfo", getIssuer());
        var response = rest.get()
                .uri(userinfoUri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class);

        var data = response.block();

        log.info("currentUser: {}", data);

        /*return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(), principal.getAttributes(), extractAuthorities(principal));*/
        Collection<GrantedAuthority> authorities;
        if (data != null) {
            authorities = extractAuthorities(data);
        } else {
            authorities = extractAuthorities(principal);
        }

        var dataMap = TransferDataMap.instance(data);

        return new UserInfo(dataMap, data, authorities);


        /*return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(), data != null ? data : principal.getAttributes(),
                authorities);*/
    }

    private Collection<GrantedAuthority> extractAuthorities(Map<String, Object> principal) {
        var authorities = principal.get(OAuth2TokenConstants.ROLES_NAME);


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
