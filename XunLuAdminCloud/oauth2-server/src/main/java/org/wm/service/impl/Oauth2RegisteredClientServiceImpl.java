package org.wm.service.impl;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wm.mapper.Oauth2RegisteredClientMapper;
import org.wm.domain.Oauth2RegisteredClient;
import org.wm.service.IOauth2RegisteredClientService;

/**
 * 授权服务器客户端注册Service业务层处理
 *
 * @author wm
 * @date 2022-06-05
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class Oauth2RegisteredClientServiceImpl implements IOauth2RegisteredClientService {

    private final  Oauth2RegisteredClientMapper oauth2RegisteredClientMapper;

    private final RegisteredClientRepository jdbcRegisteredClientRepository;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 查询授权服务器客户端注册
     * 
     * @param id 授权服务器客户端注册主键
     * @return 授权服务器客户端注册
     */
    @Override
    public Oauth2RegisteredClient selectOauth2RegisteredClientById(String id) {
        return oauth2RegisteredClientMapper.selectOauth2RegisteredClientById(id);
    }

    /**
     * 查询授权服务器客户端注册列表
     *
     * @param oauth2RegisteredClient 授权服务器客户端注册
     * @return 授权服务器客户端注册
     */
    @Override
    public PageInfo<Oauth2RegisteredClient> selectOauth2RegisteredClientList(Oauth2RegisteredClient oauth2RegisteredClient) {
        List<Oauth2RegisteredClient> list = oauth2RegisteredClientMapper.selectOauth2RegisteredClientList(oauth2RegisteredClient);
        return new PageInfo<>(list);
    }


    /**
     * 新增授权服务器客户端注册
     *
     * @param oauth2RegisteredClient 授权服务器客户端注册
     * @return 结果
     */
    @Override
    public int insertOauth2RegisteredClient(Oauth2RegisteredClient oauth2RegisteredClient) {
        try {
            // 1. 参数校验
            validateClientRequest(oauth2RegisteredClient);

            // 2. 检查客户端ID是否已存在
            RegisteredClient existingClient = jdbcRegisteredClientRepository.findByClientId(oauth2RegisteredClient.getClientId());
            if (existingClient != null) {
                log.warn("客户端ID已存在: {}", oauth2RegisteredClient.getClientId());
                throw new RuntimeException("客户端ID已存在: " + oauth2RegisteredClient.getClientId());
            }

            // 3. 构建 RegisteredClient
            RegisteredClient.Builder clientBuilder = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId(oauth2RegisteredClient.getClientId())
                    .clientName(oauth2RegisteredClient.getClientName());

            // 4. 设置客户端认证方法
            configureClientAuthenticationMethods(clientBuilder, oauth2RegisteredClient);

            // 5. 设置授权类型
            configureAuthorizationGrantTypes(clientBuilder, oauth2RegisteredClient);

            // 6. 设置重定向URI
            configureRedirectUris(clientBuilder, oauth2RegisteredClient);

            // 7. 设置Scopes
            configureScopes(clientBuilder, oauth2RegisteredClient);

            // 8. 设置客户端设置（ClientSettings）
            configureClientSettings(clientBuilder, oauth2RegisteredClient);

            // 9. 设置Token设置（TokenSettings）
            configureTokenSettings(clientBuilder, oauth2RegisteredClient);

            // 10. 保存客户端
            RegisteredClient registeredClient = clientBuilder.build();
            jdbcRegisteredClientRepository.save(registeredClient);

            log.info("客户端注册成功: clientId={}, clientName={}", oauth2RegisteredClient.getClientId(), oauth2RegisteredClient.getClientName());
            return 1;

        } catch (Exception e) {
            log.error("新增客户端失败: {}", e.getMessage(), e);
            throw new RuntimeException("新增客户端失败: " + e.getMessage(), e);
        }
    }

    /**
     * 参数校验
     */
    private void validateClientRequest(Oauth2RegisteredClient client) {
        if (!StringUtils.hasText(client.getClientId())) {
            throw new IllegalArgumentException("客户端ID不能为空");
        }
        if (!StringUtils.hasText(client.getClientName())) {
            throw new IllegalArgumentException("客户端名称不能为空");
        }
        if (!StringUtils.hasText(client.getAuthorizationGrantTypes())) {
            throw new IllegalArgumentException("授权类型不能为空");
        }
    }

    /**
     * 配置客户端认证方法
     */
    private void configureClientAuthenticationMethods(RegisteredClient.Builder builder, Oauth2RegisteredClient client) {
        String authMethods = client.getClientAuthenticationMethods();

        // 如果未指定认证方法，根据是否有密钥自动判断
        if (!StringUtils.hasText(authMethods)) {
            if (StringUtils.hasText(client.getClientSecret())) {
                // 有密钥，默认使用 CLIENT_SECRET_BASIC
                builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                builder.clientSecret(passwordEncoder.encode(client.getClientSecret()));
            } else {
                // 无密钥，公共客户端
                builder.clientAuthenticationMethod(ClientAuthenticationMethod.NONE);
            }
            return;
        }

        // 解析认证方法
        List<String> methods = Arrays.stream(authMethods.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        for (String method : methods) {
            switch (method.toLowerCase()) {
                case "none":
                    builder.clientAuthenticationMethod(ClientAuthenticationMethod.NONE);
                    break;
                case "client_secret_basic":
                    builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    if (StringUtils.hasText(client.getClientSecret())) {
                        builder.clientSecret(passwordEncoder.encode(client.getClientSecret()));
                    }
                    break;
                case "client_secret_post":
                    builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                    if (StringUtils.hasText(client.getClientSecret())) {
                        builder.clientSecret(passwordEncoder.encode(client.getClientSecret()));
                    }
                    break;
                case "client_secret_jwt":
                    builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT);
                    if (StringUtils.hasText(client.getClientSecret())) {
                        builder.clientSecret(passwordEncoder.encode(client.getClientSecret()));
                    }
                    break;
                case "private_key_jwt":
                    builder.clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT);
                    break;
                default:
                    log.warn("不支持的客户端认证方法: {}", method);
            }
        }
    }

    /**
     * 配置授权类型
     */
    private void configureAuthorizationGrantTypes(RegisteredClient.Builder builder, Oauth2RegisteredClient client) {
        List<String> grantTypes = Arrays.stream(client.getAuthorizationGrantTypes().split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        for (String grantType : grantTypes) {
            AuthorizationGrantType authGrantType = convertToAuthorizationGrantType(grantType);
            if (authGrantType != null) {
                builder.authorizationGrantType(authGrantType);
            }
        }
    }

    /**
     * 转换授权类型
     */
    private AuthorizationGrantType convertToAuthorizationGrantType(String grantType) {
        switch (grantType.toLowerCase()) {
            case "authorization_code":
                return AuthorizationGrantType.AUTHORIZATION_CODE;
            case "refresh_token":
                return AuthorizationGrantType.REFRESH_TOKEN;
            case "client_credentials":
                return AuthorizationGrantType.CLIENT_CREDENTIALS;
            case "password":
                return AuthorizationGrantType.PASSWORD;
            default:
                log.warn("不支持的授权类型: {}", grantType);
                return null;
        }
    }

    /**
     * 配置重定向URI
     */
    private void configureRedirectUris(RegisteredClient.Builder builder, Oauth2RegisteredClient client) {
        if (!StringUtils.hasText(client.getRedirectUris())) {
            return;
        }

        List<String> uris = Arrays.stream(client.getRedirectUris().split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        uris.forEach(builder::redirectUri);
    }

    /**
     * 配置Scopes
     */
    private void configureScopes(RegisteredClient.Builder builder, Oauth2RegisteredClient client) {
        // 默认添加 OIDC scopes
        builder.scope(OidcScopes.OPENID);

        if (!StringUtils.hasText(client.getScopes())) {
            return;
        }

        List<String> scopes = Arrays.stream(client.getScopes().split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        scopes.forEach(builder::scope);
    }

    /**
     * 配置客户端设置
     */
    private void configureClientSettings(RegisteredClient.Builder builder, Oauth2RegisteredClient client) {
        try {
            ClientSettings.Builder settingsBuilder = ClientSettings.builder();

            // 解析 clientSettings JSON
            if (StringUtils.hasText(client.getClientSettings())) {
                JsonNode settingsNode = objectMapper.readTree(client.getClientSettings());

                // requireAuthorizationConsent - 是否需要用户授权同意
                if (settingsNode.has("requireAuthorizationConsent")) {
                    settingsBuilder.requireAuthorizationConsent(settingsNode.get("requireAuthorizationConsent").asBoolean());
                } else {
                    // 默认值：授权码模式需要授权，其他不需要
                    boolean needsConsent = client.getAuthorizationGrantTypes().contains("authorization_code");
                    settingsBuilder.requireAuthorizationConsent(needsConsent);
                }

                // requireProofKey - 是否需要PKCE
                if (settingsNode.has("requireProofKey")) {
                    settingsBuilder.requireProofKey(settingsNode.get("requireProofKey").asBoolean());
                } else {
                    // 默认值：公共客户端需要PKCE
                    boolean needsPKCE = !StringUtils.hasText(client.getClientSecret());
                    settingsBuilder.requireProofKey(needsPKCE);
                }
            } else {
                // 未提供 clientSettings，使用默认值
                boolean needsConsent = client.getAuthorizationGrantTypes().contains("authorization_code");
                boolean needsPKCE = !StringUtils.hasText(client.getClientSecret());
                settingsBuilder.requireAuthorizationConsent(needsConsent);
                settingsBuilder.requireProofKey(needsPKCE);
            }

            builder.clientSettings(settingsBuilder.build());

        } catch (Exception e) {
            log.error("解析 clientSettings 失败: {}", e.getMessage());
            // 使用默认配置
            builder.clientSettings(ClientSettings.builder()
                    .requireAuthorizationConsent(true)
                    .requireProofKey(false)
                    .build());
        }
    }

    /**
     * 配置Token设置
     */
    private void configureTokenSettings(RegisteredClient.Builder builder, Oauth2RegisteredClient client) {
        try {
            TokenSettings.Builder settingsBuilder = TokenSettings.builder();

            // 解析 tokenSettings JSON
            if (StringUtils.hasText(client.getTokenSettings())) {
                JsonNode settingsNode = objectMapper.readTree(client.getTokenSettings());

                // accessTokenTimeToLive - Access Token 有效期
                if (settingsNode.has("accessTokenTimeToLive")) {
                    long seconds = settingsNode.get("accessTokenTimeToLive").asLong();
                    settingsBuilder.accessTokenTimeToLive(Duration.ofSeconds(seconds));
                } else {
                    settingsBuilder.accessTokenTimeToLive(Duration.ofHours(2)); // 默认2小时
                }

                // refreshTokenTimeToLive - Refresh Token 有效期
                if (settingsNode.has("refreshTokenTimeToLive")) {
                    long seconds = settingsNode.get("refreshTokenTimeToLive").asLong();
                    settingsBuilder.refreshTokenTimeToLive(Duration.ofSeconds(seconds));
                } else if (client.getAuthorizationGrantTypes().contains("refresh_token")) {
                    settingsBuilder.refreshTokenTimeToLive(Duration.ofDays(7)); // 默认7天
                }

                // reuseRefreshTokens - 是否重用 Refresh Token
                if (settingsNode.has("reuseRefreshTokens")) {
                    settingsBuilder.reuseRefreshTokens(settingsNode.get("reuseRefreshTokens").asBoolean());
                } else {
                    settingsBuilder.reuseRefreshTokens(false); // 默认不重用
                }

                // accessTokenFormat - Token 格式
                if (settingsNode.has("accessTokenFormat")) {
                    String format = settingsNode.get("accessTokenFormat").asText();
                    if ("REFERENCE".equalsIgnoreCase(format)) {
                        settingsBuilder.accessTokenFormat(OAuth2TokenFormat.REFERENCE);
                    } else {
                        settingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED); // JWT
                    }
                } else {
                    settingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED); // 默认JWT
                }

            } else {
                // 未提供 tokenSettings，使用默认值
                settingsBuilder.accessTokenTimeToLive(Duration.ofHours(2))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .reuseRefreshTokens(false)
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
            }

            builder.tokenSettings(settingsBuilder.build());

        } catch (Exception e) {
            log.error("解析 tokenSettings 失败: {}", e.getMessage());
            // 使用默认配置
            builder.tokenSettings(TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofHours(2))
                    .refreshTokenTimeToLive(Duration.ofDays(7))
                    .reuseRefreshTokens(false)
                    .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                    .build());
        }
    }

    /**
     * @deprecated 使用 convertToAuthorizationGrantType 替代
     */
    @Deprecated
    public AuthorizationGrantType toAuthorizationGrantType(String grantType) {
        return convertToAuthorizationGrantType(grantType);
    }

    /**
     * @deprecated 不再需要
     */
    @Deprecated
    private static List<AuthorizationGrantType> getGrantTypeList(List<AuthorizationGrantType> grantTypeList) {
        return grantTypeList;
    }

    /**
     * 修改授权服务器客户端注册
     * 
     * @param oauth2RegisteredClient 授权服务器客户端注册
     * @return 结果
     */
    @Override
    public int updateOauth2RegisteredClient(Oauth2RegisteredClient oauth2RegisteredClient) {
        return oauth2RegisteredClientMapper.updateOauth2RegisteredClient(oauth2RegisteredClient);
    }

    /**
     * 批量删除授权服务器客户端注册
     * 
     * @param ids 需要删除的授权服务器客户端注册主键
     * @return 结果
     */
    @Override
    public int deleteOauth2RegisteredClientByIds(String[] ids) {
        return oauth2RegisteredClientMapper.deleteOauth2RegisteredClientByIds(ids);
    }

    /**
     * 删除授权服务器客户端注册信息
     * 
     * @param id 授权服务器客户端注册主键
     * @return 结果
     */
    @Override
    public int deleteOauth2RegisteredClientById(String id) {
        return oauth2RegisteredClientMapper.deleteOauth2RegisteredClientById(id);
    }
}
