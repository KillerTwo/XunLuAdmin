package org.wm.security.authentication.jwt;

import jakarta.annotation.Nonnull;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.Assert;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.dto.TransferDataMap;
import org.wm.commons.utils.SpringContextHolder;
import org.wm.commons.web.utils.ServletUtils;
import org.wm.security.utils.ResourceServerUtils;

import java.util.Collection;

/**
 * 功能描述：<功能描述>
 * JwtAuthenticationConverter转换器自定义实现类，将jwt转换成AbstractAuthenticationToken  JWT Token转换器
 * @author dove 
 * @date 2023/08/05 21:03
 * @since 1.0
**/
public class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private String principalClaimName = JwtClaimNames.SUB;

    @Override
    public final AbstractAuthenticationToken convert(@Nonnull Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);

        String principalClaimValue = jwt.getClaimAsString(this.principalClaimName);

        var discoveryClient = SpringContextHolder.getBean(DiscoveryClient.class);
        var issuer = ResourceServerUtils.serverUrl(discoveryClient);
        var token = jwt.getTokenValue();
        var data = ResourceServerUtils.getUserInfo(issuer, token);

        var u = new LoginUser(TransferDataMap.instance(data));

        return new CustomJwtAuthenticationToken(jwt, authorities, principalClaimValue, u);
    }

    /**
     * Sets the {@link Converter Converter&lt;Jwt, Collection&lt;GrantedAuthority&gt;&gt;}
     * to use. Defaults to {@link JwtGrantedAuthoritiesConverter}.
     * @param jwtGrantedAuthoritiesConverter The converter
     * @since 5.2
     * @see JwtGrantedAuthoritiesConverter
     */
    public void setJwtGrantedAuthoritiesConverter(
            Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
    }

    /**
     * Sets the principal claim name. Defaults to {@link JwtClaimNames#SUB}.
     * @param principalClaimName The principal claim name
     * @since 5.4
     */
    public void setPrincipalClaimName(String principalClaimName) {
        Assert.hasText(principalClaimName, "principalClaimName cannot be empty");
        this.principalClaimName = principalClaimName;
    }

}
