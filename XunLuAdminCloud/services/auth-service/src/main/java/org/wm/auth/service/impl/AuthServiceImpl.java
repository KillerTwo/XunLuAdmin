package org.wm.auth.service.impl;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.wm.auth.feignClient.UserServiceClient;
import org.wm.auth.service.AuthService;
import org.wm.commons.constants.Constants;
import org.wm.commons.constants.RedisKeyConstants;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.enums.UserStatus;
import org.wm.commons.exception.ServiceException;
import org.wm.commons.exception.user.CaptchaException;
import org.wm.commons.exception.user.CaptchaExpireException;
import org.wm.commons.utils.StringUtils;
import org.wm.commons.web.utils.AddressUtils;
import org.wm.commons.web.utils.IpUtils;
import org.wm.commons.utils.uuid.IdUtils;
import org.wm.commons.web.utils.ServletUtils;
import org.wm.redis.service.RedisCache;
import org.wm.token.TokenParser;
import org.wm.token.domain.TokenPayload;

import jakarta.annotation.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：<功能描述>
 * 认证授权service
 *
 * @author dove
 * @date 2023/07/16 17:28
 * @since 1.0
 **/
@RefreshScope
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {


    private final RedisCache redisCache;

    private final UserServiceClient userServiceClient;

    private final TokenParser tokenParser;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;


    @Value("${captcha.captcha-type}")
    private String captchaType;


    /**
     * 功能描述：<功能描述>
     * 用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     验证码存储的uuid
     * @return TokenPayload jwt token信息
     * @throws ServiceException 用户验证失败
     * @author dove
     * @date 2023/7/20 00:13
     */
    @Override
    public TokenPayload login(String username, String password, String code, String uuid) {
        boolean captchaOnOff = redisCache.getCacheObject(RedisKeyConstants.CAPTCHA_ON_OFF);
        // 验证码开关
        if (captchaOnOff) {
            validateCaptcha(username, code, uuid);
        }

        var result = userServiceClient.userInfoByUsername(username);
        var user = result.getData();

        /*LoginUser sysUserVo = new LoginUser();
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);*/

        if (StringUtils.isNull(user)) {
            // TODO 需要冲洗设置调用记录日志的方式：服务调用或者消息中间件的方式
            // AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            // AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            // AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        // AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));

        // TODO 密码匹配
        /*if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            log.error("登录用户：{} , {} 密码不正确.", username, password);
            throw new ServiceException("用户不存在/密码错误");
        }*/

        // 记录客户端浏览器信息
        setUserAgent(user);
        // 生成token
        var token = tokenParser.createToken(user);
        return token;
    }


    @Override
    public Map<String, Object> captcha() {
        boolean captchaOnOff = redisCache.getCacheObject(RedisKeyConstants.CAPTCHA_ON_OFF);
        Map<String, Object> map = new HashMap<>();
        map.put("captchaOnOff", captchaOnOff);
        if (!captchaOnOff) {
            return map;
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        map.put("code", capStr);
        map.put("uuid", uuid);
        map.put("img", Base64.encode(os.toByteArray()));
        return map;
    }


    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            // TODO 需要冲洗设置调用记录日志的方式：服务调用或者消息中间件的方式
            // AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            // AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }


}
