package org.wm.security.authentication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.dto.TransferDataMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 功能描述：<功能描述>
 * opeque token 认证对象
 * @author dove 
 * @date 2024/03/06 19:37
 * @since 1.0
**/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfo extends LoginUser implements OAuth2AuthenticatedPrincipal {


    private final Map<String, Object> attributes;

    private final Collection<GrantedAuthority> authorities;

    public UserInfo(TransferDataMap dataMap, Map<String, Object> attributes, Collection<GrantedAuthority> authorities) {
        super(dataMap);
        this.attributes = Collections.unmodifiableMap(attributes);
        this.authorities = (authorities != null) ? Collections.unmodifiableCollection(authorities)
                : AuthorityUtils.NO_AUTHORITIES;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}
