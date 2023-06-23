package org.wm.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wm.entity.SysUser;
import org.wm.entity.vo.LoginUser;
import org.wm.enums.UserStatus;
import org.wm.exception.ServiceException;
import org.wm.service.ISysUserService;
import org.wm.utils.StringUtils;

/**
 * 用户验证处理
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String usernameOrPhone) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(usernameOrPhone);
        if (StringUtils.isNull(user)) {
            user = userService.selectUserByMobile(usernameOrPhone);
        }
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", usernameOrPhone);
            throw new ServiceException("登录用户：" + usernameOrPhone + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", usernameOrPhone);
            throw new ServiceException("对不起，您的账号：" + usernameOrPhone + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", usernameOrPhone);
            throw new ServiceException("对不起，您的账号：" + usernameOrPhone + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
