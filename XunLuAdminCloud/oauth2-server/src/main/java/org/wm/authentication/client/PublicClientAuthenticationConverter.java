package org.wm.authentication.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * 功能描述：公共客户端认证转换器
 * 用于支持不需要client_secret的公共客户端（如SPA应用使用密码模式）
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 **/
public class PublicClientAuthenticationConverter implements AuthenticationConverter {

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        // 只处理 /oauth2/token 端点
        if (!"/oauth2/token".equals(request.getRequestURI())) {
            return null;
        }

        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // 获取 client_id
        String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID);
        if (!StringUtils.hasText(clientId)) {
            return null;
        }

        // 检查是否已经有其他认证方式（如 Basic Auth）
        if (request.getHeader("Authorization") != null) {
            return null;
        }

        // 检查是否提供了 client_secret
        String clientSecret = parameters.getFirst(OAuth2ParameterNames.CLIENT_SECRET);
        if (StringUtils.hasText(clientSecret)) {
            // 如果提供了 client_secret，让其他认证器处理
            return null;
        }

        // 获取授权类型
        String grantType = parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE);
        if (!StringUtils.hasText(grantType)) {
            return null;
        }

        // 仅支持密码模式和刷新令牌模式的公共客户端
        if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType) &&
            !AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(grantType)) {
            return null;
        }


        var maps = new HashMap<String, Object>(parameters);

        // 创建公共客户端认证令牌
        return new PublicClientAuthenticationToken(clientId, ClientAuthenticationMethod.NONE, null, maps);
    }
}
