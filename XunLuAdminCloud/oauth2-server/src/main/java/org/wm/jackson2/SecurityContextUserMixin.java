package org.wm.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * 功能描述：<功能描述>
 *
 *     Jackson mixin class to serialize/deserialize
 *  * {@link org.wm.domain.dto.SecurityContextUser}.
 *
 * @author dove 
 * @date 2023/08/06 23:23
 * @since 1.0
**/

/*@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY)*/
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class SecurityContextUserMixin {

    @JsonCreator
    public SecurityContextUserMixin(Long userId, Long deptId, String token,
                                    Long loginTime, Long expireTime, String ipaddr,
                                    String loginLocation, String browser, String os,
                                    Set<String> permissions, String username,
                                    String nickName, String email, String phonenumber,
                                    String sex, String avatar, String password,
                                    String salt, String status, String delFlag,
                                    String loginIp, Date loginDate, Long[] roleIds,
                                    Long[] postIds, Set<String> roles, Collection<? extends GrantedAuthority> authorities) {

    }
}
