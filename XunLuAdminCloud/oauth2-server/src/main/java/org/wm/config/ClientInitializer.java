package org.wm.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

/**
 * 功能描述：OAuth2客户端初始化器
 * 用于初始化系统默认的OAuth2客户端
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientInitializer implements ApplicationRunner {

    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始初始化OAuth2客户端配置...");

        // 1. 初始化内部管理后台客户端（用于本系统前端登录）
        initInternalAdminClient();

        // 2. 初始化示例第三方应用客户端（演示用）
        initDemoThirdPartyClient();

        // 3. 初始化微服务客户端（用于服务间调用）
        initMicroserviceClient();

        log.info("OAuth2客户端初始化完成！");
    }

    /**
     * 内部管理后台客户端
     * 使用场景：本系统的React前端登录
     * 授权模式：密码模式(Password Grant) - 仅限内部使用
     */
    private void initInternalAdminClient() {
        String clientId = "xunlu-admin-web";

        // 检查客户端是否已存在
        RegisteredClient existingClient = registeredClientRepository.findByClientId(clientId);

        RegisteredClient client = RegisteredClient.withId(existingClient != null ? existingClient.getId() : UUID.randomUUID().toString())
                .clientId(clientId)
                // 内部客户端使用密码模式，不需要client_secret（公共客户端）
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                // 支持的授权模式
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // 客户端设置
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)  // 内部客户端不需要用户授权
                        .requireProofKey(false)              // 密码模式不使用PKCE
                        .build())
                // Token设置
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(2))    // Access Token 2小时
                        .refreshTokenTimeToLive(Duration.ofDays(7))    // Refresh Token 7天
                        .reuseRefreshTokens(false)                     // 不重用refresh token
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)  // 使用JWT
                        .build())
                // Scope - 必须包含 openid 以支持 /oauth2/userinfo 端点
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .scope("all")
                .scope("user.read")
                .scope("user.write")
                .build();

        registeredClientRepository.save(client);

        if (existingClient != null) {
            log.info("内部管理后台客户端配置已更新: {}", clientId);
        } else {
            log.info("内部管理后台客户端初始化成功: {}", clientId);
        }
    }

    /**
     * 示例第三方应用客户端
     * 使用场景：演示外部应用如何接入本认证平台
     * 授权模式：授权码模式(Authorization Code + PKCE)
     */
    private void initDemoThirdPartyClient() {
        String clientId = "demo-third-party-app";

        // 检查客户端是否已存在
        RegisteredClient existingClient = registeredClientRepository.findByClientId(clientId);

        // 第三方应用需要client_secret（机密客户端）
        String clientSecret = passwordEncoder.encode("demo-secret-change-in-production");

        RegisteredClient client = RegisteredClient.withId(existingClient != null ? existingClient.getId() : UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(clientSecret)
                // 机密客户端使用Basic认证
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                // 授权码模式 + PKCE
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // 回调地址（实际使用时需要配置真实的应用地址）
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/xunlu")
                .redirectUri("http://localhost:8080/authorized")
                .redirectUri("https://oauth.pstmn.io/v1/callback")  // Postman测试用
                // OIDC支持
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .scope("user.read")
                // 客户端设置
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)   // 需要用户授权同意
                        .requireProofKey(true)               // 强制使用PKCE增强安全性
                        .build())
                // Token设置
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))  // Access Token 30分钟
                        .refreshTokenTimeToLive(Duration.ofDays(30))    // Refresh Token 30天
                        .reuseRefreshTokens(false)
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)  // JWT
                        .build())
                .build();

        registeredClientRepository.save(client);

        if (existingClient != null) {
            log.info("演示第三方应用客户端配置已更新: {}", clientId);
        } else {
            log.info("演示第三方应用客户端初始化成功: {}, clientSecret: {}", clientId, "demo-secret-change-in-production");
        }
    }

    /**
     * 微服务客户端
     * 使用场景：服务间调用认证
     * 授权模式：客户端凭证模式(Client Credentials)
     */
    private void initMicroserviceClient() {
        String clientId = "microservice-internal";

        // 检查客户端是否已存在
        RegisteredClient existingClient = registeredClientRepository.findByClientId(clientId);

        String clientSecret = passwordEncoder.encode("microservice-secret-change-in-production");

        RegisteredClient client = RegisteredClient.withId(existingClient != null ? existingClient.getId() : UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 客户端凭证模式
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                // 微服务Scope
                .scope("service.call")
                .scope("internal.api")
                // 客户端设置
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                // Token设置
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))  // 1小时
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .build())
                .build();

        registeredClientRepository.save(client);

        if (existingClient != null) {
            log.info("微服务客户端配置已更新: {}", clientId);
        } else {
            log.info("微服务客户端初始化成功: {}, clientSecret: {}", clientId, "microservice-secret-change-in-production");
        }
    }
}
