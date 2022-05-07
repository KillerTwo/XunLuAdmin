package org.wm.authentication;

import javax.annotation.Resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.wm.config.thread.AsyncManager;
import org.wm.constants.Constants;
import org.wm.entity.SysUser;
import org.wm.entity.vo.LoginUser;
import org.wm.entity.vo.PhoneCodeBody;
import org.wm.entity.vo.ResetPasswordBody;
import org.wm.exception.ServiceException;
import org.wm.exception.user.CaptchaException;
import org.wm.exception.user.CaptchaExpireException;
import org.wm.exception.user.UserPasswordNotMatchException;
import org.wm.service.ISysConfigService;
import org.wm.service.ISysUserService;
import org.wm.threads.AsyncFactory;
import org.wm.utils.*;
import org.wm.utils.ip.IpUtils;

import java.util.Date;

/**
 * 登录校验方法
 */
@RequiredArgsConstructor
@Component
public class SysLoginService {
    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    private final RedisCache redisCache;

    private final ISysUserService userService;


    private final ISysConfigService configService;

    private final ISysUserService sysUserService;

    private final PasswordEncoder passwordEncoder;

    /**
     *  重置密码
     * @param resetPasswordBody 请求参数
     */
    public void resetPassword(ResetPasswordBody resetPasswordBody) {
        // 验证短信验证码
        // 修改用户密码
        String password = resetPasswordBody.getPassword();
        if (StringUtils.isEmpty(password) || !password.equals(resetPasswordBody.getRePassword())) {
            throw new ServiceException("两次输入的密码不一致");
        }

        SysUser sysUser = sysUserService.selectUserByUserName(resetPasswordBody.getPhone());
        if (sysUser == null) {
            throw new ServiceException("用户不存在！");
        }

        String encodePassword = passwordEncoder.encode(password);
        sysUser.setPassword(encodePassword);
        sysUser.setUpdateTime(new Date());
        sysUserService.updateUser(sysUser);
    }

    /**
     * 发送验证码
     * @param phoneCodeBody 参数
     * @return 验证码
     */
    public String getPhoneCode(PhoneCodeBody phoneCodeBody) {
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        // 验证码开关
        if (captchaOnOff) {
            validateCaptcha(phoneCodeBody.getPhone(), phoneCodeBody.getCode(), phoneCodeBody.getUuid());
        }
        // 发送验证码
        return "123456";
    }

    /**
     *  用户登录，没有验证码
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    public String login(String username, String password) {
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        // 验证码开关
        if (captchaOnOff) {
            validateCaptcha(username, code, uuid);
        }
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
