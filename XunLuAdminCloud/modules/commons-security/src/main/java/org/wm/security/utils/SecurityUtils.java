package org.wm.security.utils;


import io.micrometer.core.instrument.util.StringUtils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.wm.commons.constants.TokenConstants;
import org.wm.commons.dto.LoginUser;

import jakarta.servlet.http.HttpServletRequest;
import org.wm.commons.utils.SpringContextHolder;
import org.wm.commons.web.utils.ServletUtils;
import org.wm.redis.service.RedisCache;
import org.wm.security.feignClient.UserServiceClient;

import java.util.concurrent.TimeUnit;

/**
 * 功能描述：<功能描述>
 * 安全服务工具类
 *
 * @author dove
 * @date 2023/7/12 23:35
 * @since 1.0
 **/
public class SecurityUtils {

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        // TODO
        return getLoginUser().getUserId();
    }

    /**
     * 获取用户名称
     */
    public static String getUsername() {
        // TODO
        var user = SecurityContextHolder.getContext().getAuthentication();

        var principal = (Jwt) user.getPrincipal();

        var username = principal.getClaim("sub");
        return username != null ? String.valueOf(username): null;
    }


    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser() {
        // 通过查询数据库的方式 TODO 需要验证
        var user = SecurityContextHolder.getContext().getAuthentication();

        var redisCache = SpringContextHolder.getBean(RedisCache.class);
        if (user != null) {
            var principal = (Jwt) user.getPrincipal();
            var username = principal.getClaim("sub");
            var userKey = "LOGIN_USER:" + username;
            LoginUser sysUser = redisCache.getCacheObject(userKey);
            if (sysUser != null) {
                return sysUser;
            }
            var userServiceClient = SpringContextHolder.getBean(UserServiceClient.class);
            var res = userServiceClient.userInfoByUsername(String.valueOf(username));
            sysUser = res.getData();

            // TODO 需要配置当前用户过期时间
            redisCache.setCacheObject(userKey, sysUser, 30, TimeUnit.MINUTES);
            return sysUser;
        }
        return null;
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     * TODO 改为使用Spring Security验证 需要删除
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        /*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);*/
        return password;
    }

    /**
     * 判断密码是否相同
     * TODO 改为使用Spring Security验证 需要删除
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        /*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);*/
        return false;
    }

}
