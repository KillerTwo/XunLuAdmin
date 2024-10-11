package org.wm.generator.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.web.client.RestOperations;
import org.springframework.web.reactive.function.client.WebClient;
import org.wm.generator.constant.OAuth2TokenConstants;
import org.wm.generator.domain.TransferDataMap;
import org.wm.generator.domain.UserInfo;
import org.wm.generator.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
        Collection<GrantedAuthority> authorities = null;
        if (data != null) {
            authorities = extractAuthorities(data);
        }

        var dataMap = TransferDataMap.instance(data);

        return new UserInfo(dataMap, data, authorities);
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
}
