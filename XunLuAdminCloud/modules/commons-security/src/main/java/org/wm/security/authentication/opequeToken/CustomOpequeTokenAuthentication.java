package org.wm.security.authentication.opequeToken;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * 功能描述：<功能描述>
 * OpequeToken 认证对象
 * @author dove 
 * @date 2024/03/06 22:56
 * @since 1.0
**/
public class CustomOpequeTokenAuthentication extends AbstractOAuth2TokenAuthenticationToken<OAuth2AccessToken> {

    public CustomOpequeTokenAuthentication(Object principal, OAuth2AccessToken credentials,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(credentials, principal, credentials, authorities);
        Assert.isTrue(credentials.getTokenType() == OAuth2AccessToken.TokenType.BEARER,
                "credentials must be a bearer token");
        setAuthenticated(true);
    }


    @Override
    public Map<String, Object> getTokenAttributes() {
        return null;
    }
}
