package org.wm.authentication.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * 功能描述：公共客户端认证Provider
 * 验证公共客户端（不需要client_secret的客户端）
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 **/
@Slf4j
public class PublicClientAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";

    private final RegisteredClientRepository registeredClientRepository;

    public PublicClientAuthenticationProvider(RegisteredClientRepository registeredClientRepository) {
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PublicClientAuthenticationToken publicClientAuthentication =
                (PublicClientAuthenticationToken) authentication;

        // 仅支持 ClientAuthenticationMethod.NONE
        if (!ClientAuthenticationMethod.NONE.equals(publicClientAuthentication.getClientAuthenticationMethod())) {
            return null;
        }

        String clientId = publicClientAuthentication.getPrincipal().toString();

        // 从仓库中查找注册的客户端
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throwInvalidClient("client_not_found");
        }

        if (log.isTraceEnabled()) {
            log.trace("Retrieved registered client: {}", registeredClient.getId());
        }

        // 验证客户端是否配置为公共客户端
        if (!registeredClient.getClientAuthenticationMethods().contains(ClientAuthenticationMethod.NONE)) {
            log.warn("客户端 {} 不是公共客户端，不支持无密钥认证", clientId);
            throwInvalidClient("client_authentication_method_not_supported");
        }

        // 验证客户端密钥是否为空（公共客户端不应有密钥）
        if (registeredClient.getClientSecret() != null) {
            log.warn("客户端 {} 配置了client_secret，但尝试使用公共客户端认证", clientId);
            throwInvalidClient("invalid_client_configuration");
        }

        if (log.isDebugEnabled()) {
            log.debug("公共客户端认证成功: {}", clientId);
        }

        // 创建已认证的客户端令牌
        return new PublicClientAuthenticationToken(
                registeredClient,
                ClientAuthenticationMethod.NONE,
                null
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PublicClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static void throwInvalidClient(String errorCode) {
        OAuth2Error error = new OAuth2Error(
                OAuth2ErrorCodes.INVALID_CLIENT,
                "Client authentication failed: " + errorCode,
                ERROR_URI
        );
        throw new OAuth2AuthenticationException(error);
    }
}
