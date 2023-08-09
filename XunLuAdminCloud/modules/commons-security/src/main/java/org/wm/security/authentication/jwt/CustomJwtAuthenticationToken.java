package org.wm.security.authentication.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

/**
 * 功能描述：<功能描述>
 *     存储Security认证对象，从jwt token解析所得
 *
 * @author dove 
 * @date 2023/08/05 21:11
 * @since 1.0
**/
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {
    public CustomJwtAuthenticationToken(Jwt jwt) {
        super(jwt);
    }

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities, name);
    }
}
