package org.wm.utils;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.wm.commons.dto.LoginUser;

/**
 * 功能描述：<功能描述>
 * OidcUserInfo相关工具
 * @author dove 
 * @date 2024/03/04 23:10
 * @since 1.0
**/
public class UserInfoMapperUtils {


    public static OidcUserInfo loginUserToOidcUserInfo(LoginUser loginUser) {
        var user = OidcUserInfo.builder()
                .claim("userId", loginUser.getUserId())
                .claim("deptId", loginUser.getDeptId())
                .claim("token", loginUser.getToken())
                .claim("permissions", loginUser.getPermissions())
                .claim("nickName", loginUser.getNickName())
                .claim("email", loginUser.getEmail())
                .claim("phonenumber", loginUser.getPhonenumber())
                .claim("sex", loginUser.getSex())
                .claim("avatar", loginUser.getAvatar())
                .claim("status", loginUser.getStatus())
                .claim("delFlag", loginUser.getDelFlag())
                .claim("roleIds", loginUser.getRoleIds())
                .claim("postIds", loginUser.getPostIds())
                .claim("roles", loginUser.getRoles())
                .build();
        return user;
    }

}
