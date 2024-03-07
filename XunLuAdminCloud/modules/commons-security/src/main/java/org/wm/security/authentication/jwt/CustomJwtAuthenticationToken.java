package org.wm.security.authentication.jwt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Map;

/**
 * 功能描述：<功能描述>
 *     存储Security认证对象，从jwt token解析所得
 *
 * @author dove 
 * @date 2023/08/05 21:11
 * @since 1.0
**/
@Getter
@Setter
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {

    private Object principal;

    public CustomJwtAuthenticationToken(Jwt jwt) {
        super(jwt);
    }

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name,
                                        Object data) {
        super(jwt, authorities, name);
        this.principal = data;
    }
}
