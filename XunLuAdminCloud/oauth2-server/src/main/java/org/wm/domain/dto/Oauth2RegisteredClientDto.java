package org.wm.domain.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 功能描述：<功能描述>
 *     客户端信息
 * @author dove
 * @date 2023/7/31 15:31
 * @since 1.0
 **/
@Data
public class Oauth2RegisteredClientDto {

    private String id;

    private String clientId;

    private Date clientIdIssuedAt;

    private String clientSecret;

    private Date clientSecretExpiresAt;

    private String clientName;

    private String clientAuthenticationMethods;

    private List<String> authorizationGrantTypes;

    private String redirectUris;

    private List<String> scopes;

    private String clientSettings;

    private String tokenSettings;
}
