package org.wm.authentication.password;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;
import java.util.Set;

/**
 * @创建人 sk
 * @创建时间 2022/6/9
 * @描述
 */
public class PasswordOAuth2AuthorizationGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private Set<String> scopes;

    /**
     * Sub-class constructor.
     *
     * @param authorizationGrantType the authorization grant type
     * @param clientPrincipal        the authenticated client principal
     * @param additionalParameters   the additional parameters
     */
    protected PasswordOAuth2AuthorizationGrantAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                                                  Authentication clientPrincipal,
                                                                  Map<String, Object> additionalParameters,
                                                                  Set<String> scopes) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
        this.scopes = scopes;
    }

    public Set<String> getScopes() {
        return scopes;
    }
}
