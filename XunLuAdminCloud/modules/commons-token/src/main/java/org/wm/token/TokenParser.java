package org.wm.token;

import com.nimbusds.jwt.JWTClaimsSet;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wm.commons.constants.SecurityConstants;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.exception.ServiceException;
import org.wm.commons.utils.*;

import org.wm.redis.service.RedisCache;
import org.wm.token.domain.TokenPayload;
import org.wm.token.jks.jwt.NimbusdsJwtTokenGenerator;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：<功能描述>
 * token处理类
 *
 * @author dove
 * @date 2023/7/12 23:55
 * @since 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenParser {

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;


    private final RedisCache redisCache;


    private final NimbusdsJwtTokenGenerator tokenGenerator;


    /**
     * 功能描述：<功能描述>
     * 通过token获取用户信息
     *
     * @param token token信息
     * @return org.wm.commons.dto.LoginUser
     * @author dove
     * @date 2023/7/12 23:57
     */
    public LoginUser getLoginUser(String token) {
        // 获取请求携带的令牌
        if (StringUtils.isNotEmpty(token)) {
            try {
                JWTClaimsSet claims = parseToken(token);
                if (claims == null) {
                    return null;
                }
                // 解析对应的权限以及用户信息
                var object = redisCache.getCacheObject(token);
                String s = ObjectMapperUtil.writeValueAsString(object);
                LoginUser user = ObjectMapperUtil.readValue(s, LoginUser.class);
                return user;
            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            redisCache.deleteObject(token);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public TokenPayload createToken(LoginUser loginUser) {
        // String token = IdUtils.fastUUID();


        Map<String, Object> claims = Map.of(SecurityConstants.DETAILS_USERNAME, loginUser.getUsername(),
                SecurityConstants.DETAILS_USER_ID, loginUser.getUserId());


        var accessToken = createToken(claims);

        loginUser.setToken(accessToken);

        // 设置redis缓存
        var expireTime = refreshToken(loginUser);

        var token = TokenPayload.builder()
                .accessToken(accessToken)
                .expireTime(expireTime)
                .refreshToken("")
                .build();
        return token;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public Long refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        // 令牌有效期（默认30分钟）
        int expireTime = 30;
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);

        // 以jwt token为key将登录用户缓存到redis
        redisCache.setCacheObject(loginUser.getToken(), loginUser, expireTime, TimeUnit.MINUTES);
        return loginUser.getExpireTime();
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(JWTClaimsSet claims) {
        return tokenGenerator.createToken(claims);
    }

    /**
     * 功能描述：<功能描述>
     *       创建jwt token
     * @author dove
     * @date 2023/7/25 22:46
     * @param claims Map
     * @return java.lang.String
     */
    private String createToken(Map<String, Object> claims) {
        return tokenGenerator.createToken(claims);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public JWTClaimsSet parseToken(String token) {
        return tokenGenerator.parseToken(token);
    }


    /*private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }*/

    /**
     * 功能描述：<功能描述>
     * 从jwt token中获取用户ID
     *
     * @param claims jwt
     * @return java.lang.String
     * @author dove
     * @date 2023/7/15 20:19
     */
    public String getUserId(JWTClaimsSet claims) {
        return Convert.toStr(tokenGenerator.getValue(claims, SecurityConstants.DETAILS_USER_ID), "");
    }


    /**
     * 功能描述：<功能描述>
     *       从jwt token中获取用户名
     * @author dove
     * @date 2023/7/15 20:21
     * @param claims  jwt
     * @return java.lang.String
     */
    public String getUserName(JWTClaimsSet claims) {
        return Convert.toStr(tokenGenerator.getValue(claims, SecurityConstants.DETAILS_USERNAME), "");
    }
}
