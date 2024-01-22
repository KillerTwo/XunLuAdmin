package org.wm.auth.enums;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import org.springframework.util.FastByteArrayOutputStream;
import org.wm.auth.code.captcha.CaptchaRes;
import org.wm.commons.constants.Constants;
import org.wm.commons.exception.ServiceException;
import org.wm.commons.utils.uuid.IdUtils;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.IOException;

/**
 * 功能描述：<功能描述>
 *  生成字符验证码和数字验证码
 * @author dove 
 * @date 2024/01/22 23:37
 * @since 1.0
**/
public enum CaptchaGeneratorEnum {

    CHAR_CAPTCHA() {
        @Override
        public CaptchaRes generate(Producer captchaProducer) {
            var capStr = captchaProducer.createText();
            var code = capStr;
            var image = captchaProducer.createImage(capStr);

            var base64 = generateBase64(image);

            var captchaRes = new CaptchaRes();
            captchaRes.setCode(code);
            captchaRes.setKey(generateKey());
            captchaRes.setImageBase64(base64);

            return captchaRes;
        }
    },

    MATH_CAPTCHA() {
        @Override
        public CaptchaRes generate(Producer mathProducerMath) {
            String capText = mathProducerMath.createText();
            var capStr = capText.substring(0, capText.lastIndexOf("@"));
            var code = capText.substring(capText.lastIndexOf("@") + 1);
            var image = mathProducerMath.createImage(capStr);


            var base64 = generateBase64(image);

            var captchaRes = new CaptchaRes();
            captchaRes.setCode(code);
            captchaRes.setKey(generateKey());
            captchaRes.setImageBase64(base64);
            return captchaRes;
        }
    };


    public abstract CaptchaRes generate(Producer producer);


    public String generateKey() {
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        return Constants.CAPTCHA_CODE_KEY + uuid;
    }


    public String generateBase64(RenderedImage image) {
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }

        var base64 = Base64.encode(os.toByteArray());
        return base64;
    }

}
