package org.wm.auth.service.impl;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.context.request.ServletWebRequest;
import org.wm.auth.code.captcha.enums.CaptchaType;
import org.wm.auth.code.captcha.facade.CaptchaFacade;
import org.wm.auth.feignClient.UserServiceClient;
import org.wm.auth.service.ValidatorService;
import org.wm.commons.constants.Constants;
import org.wm.commons.constants.RedisKeyConstants;
import org.wm.commons.exception.ServiceException;
import org.wm.commons.exception.user.CaptchaException;
import org.wm.commons.exception.user.CaptchaExpireException;
import org.wm.commons.utils.StringUtils;
import org.wm.commons.utils.uuid.IdUtils;
import org.wm.redis.service.RedisCache;
import org.wm.token.TokenParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：<功能描述>
 * 验证器Service
 * @author dove 
 * @date 2024/01/22 00:20
 * @since 1.0
**/
@RefreshScope
@Slf4j
@RequiredArgsConstructor
@Service
public class ValidatorServiceImpl implements ValidatorService {

    private final RedisCache redisCache;

    private final UserServiceClient userServiceClient;

    private final TokenParser tokenParser;

    private final CaptchaFacade captchaFacade;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;


    @Value("${captcha.captcha-type}")
    private String captchaType;

    @Override
    public Map<String, Object> captcha(HttpServletRequest request, @Nullable HttpServletResponse response) {
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

        // 生成验证码  TODO 取消这个方式
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        /// TODO 需要使用这个方式生成验证码
        /*var res = captchaFacade.obtainCodeBase64(CaptchaType.getCaptchaType(captchaType),
                new ServletWebRequest(request, response), verifyKey);*/

        // BufferedImage image = res.getImageBase64();

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
    @Override
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

}
