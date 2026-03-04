package org.wm.authentication.client;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 功能描述：公共客户端认证Token
 * 用于表示不需要client_secret的公共客户端认证
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 **/
@Transient
public class PublicClientAuthenticationToken extends OAuth2ClientAuthenticationToken {

    /**
     * 构造公共客户端认证令牌
     *
     * @param clientId 客户端ID
     * @param clientAuthenticationMethod 认证方法（NONE）
     * @param credentials 凭证（公共客户端为null）
     * @param additionalParameters 附加参数
     */
    public PublicClientAuthenticationToken(String clientId,
                                           ClientAuthenticationMethod clientAuthenticationMethod,
                                           @Nullable Object credentials,
                                           @Nullable Map<String, Object> additionalParameters) {
        super(clientId, clientAuthenticationMethod, credentials, additionalParameters);
        Assert.hasText(clientId, "clientId cannot be empty");
    }

    /**
     * 构造已认证的公共客户端令牌
     *
     * @param registeredClient 注册的客户端
     * @param clientAuthenticationMethod 认证方法
     * @param credentials 凭证
     */
    public PublicClientAuthenticationToken(RegisteredClient registeredClient,
                                           ClientAuthenticationMethod clientAuthenticationMethod,
                                           @Nullable Object credentials) {
        super(registeredClient, clientAuthenticationMethod, credentials);
    }
}
