package org.wm.authentication;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wm.constants.Constants;
import org.wm.entity.vo.LoginUser;
import org.wm.exception.ServiceException;
import org.wm.jks.RSAGenerator;
import org.wm.utils.ObjectMapperUtil;
import org.wm.utils.RedisCache;
import org.wm.utils.ServletUtils;
import org.wm.utils.StringUtils;
import org.wm.utils.ip.AddressUtils;
import org.wm.utils.ip.IpUtils;
import org.wm.utils.uuid.IdUtils;

/**
 * token验证处理
 */
@Component
public class TokenService {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RSAGenerator rsaGenerator;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                JWTClaimsSet claims = parseToken(token);
                if (claims == null) {
                    return null;
                }
                // 解析对应的权限以及用户信息
                String uuid = claims.getStringClaim(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                var object = redisCache.getCacheObject(userKey);
                String s = ObjectMapperUtil.writeValueAsString(object);
                // LoginUser user = redisCache.getCacheObject(userKey);
                LoginUser user = ObjectMapperUtil.readValue(s, LoginUser.class);
                return user;
            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);
        var second = LocalDateTime.now().plusMinutes(30).toEpochSecond(ZoneOffset.UTC);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(loginUser.getUsername())
                .jwtID(IdUtils.fastSimpleUUID())
                .expirationTime(new Date(second * 1000))
                .claim(Constants.LOGIN_USER_KEY, token)
                .build();
        return createToken(claimsSet);
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
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(JWTClaimsSet claims) {
        /*String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();*/

        var rsaJWK = rsaGenerator.getRsaKey();
        try {
            JWSSigner signer = new RSASSASigner(rsaJWK);


            JWSHeader jwsHeader = new JWSHeader
                    .Builder(JWSAlgorithm.RS256)    // 指定 RSA 算法
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    jwsHeader,
                    claims);

            signedJWT.sign(signer);

            String token = signedJWT.serialize();
            return token;
        } catch (JOSEException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private JWTClaimsSet parseToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        System.err.println("token: " + token);
        try {
            var decodeJWT = SignedJWT.parse(token);

            var rsaJWK = rsaGenerator.getRsaKey();
            var rsaPublicJWK = rsaJWK.toPublicJWK();

            JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);
            if (decodeJWT.verify(verifier)) {
                System.err.println(decodeJWT.getJWTClaimsSet());
                return decodeJWT.getJWTClaimsSet();
            }
            return null;
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            // throw new ServiceException(e.getMessage());
            return null;
        }
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        var claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }
}
