package org.wm.domain.vo;

import lombok.Data;
import java.time.Instant;
import java.util.Set;

/**
 * 功能描述：注册客户端VO
 * 用于展示客户端信息（不包含敏感的client_secret）
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 **/
@Data
public class RegisteredClientVO {

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端ID颁发时间
     */
    private Instant clientIdIssuedAt;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 客户端密钥（脱敏显示，如 ****abc123）
     */
    private String clientSecretMasked;

    /**
     * 客户端认证方法
     */
    private Set<String> clientAuthenticationMethods;

    /**
     * 授权模式
     */
    private Set<String> authorizationGrantTypes;

    /**
     * 重定向URI列表
     */
    private Set<String> redirectUris;

    /**
     * 授权范围
     */
    private Set<String> scopes;

    /**
     * 客户端类型（third_party_app, internal_admin, microservice等）
     */
    private String clientType;

    /**
     * 是否需要用户授权同意
     */
    private Boolean requireAuthorizationConsent;

    /**
     * Access Token有效期（秒）
     */
    private Long accessTokenTimeToLive;

    /**
     * Refresh Token有效期（秒）
     */
    private Long refreshTokenTimeToLive;

    /**
     * 创建时间
     */
    private Instant createdAt;

    /**
     * 状态（启用/禁用）
     */
    private String status;
}
