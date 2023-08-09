package org.wm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.dto.TransferDataMap;
import org.wm.commons.enums.UserStatus;
import org.wm.commons.exception.ServiceException;
import org.wm.commons.utils.StringUtils;
import org.wm.domain.dto.SecurityContextUser;
import org.wm.feignClient.UserServiceClient;

/**
 * 功能描述：<功能描述>
 *     UserDetailsService实现类，调用system模块查询用户信息
 *
 * @author dove 
 * @date 2023/08/05 22:17
 * @since 1.0
**/
// @Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2UserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var result = userServiceClient.userInfoByUsername(username);

        var user = result.getData();

        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
            // throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        return createSecurityContextUser(user);
    }

    public UserDetails createSecurityContextUser(LoginUser user) {
        var data = TransferDataMap.instance(user);
        return new SecurityContextUser(data);
    }




}
