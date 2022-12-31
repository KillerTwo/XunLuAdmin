package org.wm.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.*;
import org.wm.annotation.Log;
import org.wm.authentication.SysLoginService;
import org.wm.authentication.SysPermissionService;
import org.wm.constants.Constants;
import org.wm.entity.SysMenu;
import org.wm.entity.SysUser;
import org.wm.entity.vo.LoginBody;
import org.wm.entity.vo.PhoneCodeBody;
import org.wm.entity.vo.ResetPasswordBody;
import org.wm.entity.vo.RouterReactVo;
import org.wm.properties.AppProperties;
import org.wm.response.ResponseResult;
import org.wm.service.ISysConfigService;
import org.wm.service.ISysMenuService;
import org.wm.utils.RedisCache;
import org.wm.utils.SecurityUtils;
import org.wm.utils.StringUtils;
import org.wm.utils.uuid.IdUtils;

import jakarta.annotation.Resource;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;


/**
 * 登录验证
 *
 * @author sk
 */
@RequiredArgsConstructor
@RestController
public class SysLoginController {
    private final SysLoginService loginService;

    private final ISysMenuService menuService;

    private final SysPermissionService permissionService;

    private final ISysConfigService sysConfigService;

    private final AppProperties appProperties;

    private final RedisCache redisCache;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public ResponseResult<?> getCode(HttpServletResponse response) throws IOException {
        boolean captchaOnOff = sysConfigService.selectCaptchaOnOff();
        Map<String, Object> map = new HashMap<>();
        map.put("captchaOnOff", captchaOnOff);
        if (!captchaOnOff) {
            return ResponseResult.success(map);
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = appProperties.getCaptchaType();
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
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e) {
            return ResponseResult.error(e.getMessage());
        }
        map.put("uuid", uuid);
        map.put("img", Base64.encode(os.toByteArray()));
        return ResponseResult.success(map);
    }

    /**
     * 获取短信验证码
     */
    @PostMapping("captcha")
    public ResponseResult<String> getPhoneCode(@RequestBody  PhoneCodeBody phoneCodeBody) {
        String phoneCode = loginService.getPhoneCode(phoneCodeBody);
        return ResponseResult.success("验证码发送成功", phoneCode);
    }

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public ResponseResult<Map<String, Object>> login(@RequestBody LoginBody loginBody) {
        // 生成令牌
        String token;
        if(StringUtils.isEmpty(loginBody.getCode())) {
            token = loginService.login(loginBody.getUsername(), loginBody.getPassword());
        } else {
            token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                    loginBody.getUuid());
        }
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TOKEN, token);
        return ResponseResult.success(map);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public ResponseResult<Map<String, Object>> getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ResponseResult.success(ajax);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public ResponseResult<List<RouterReactVo>> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        // return ResponseResult.success(menuService.buildMenus(menus));
        return ResponseResult.success(menuService.buildMenusReact(menus));
    }

    /**
     * 重置密码
     *
     * @param resetPasswordBody 请求参数
     * @return 结果
     */
    @Log(title = "重置密码")
    @PostMapping("/resetPassword")
    public ResponseResult<Map<String, Object>> resetPassword(@RequestBody ResetPasswordBody resetPasswordBody) {
        loginService.resetPassword(resetPasswordBody);
        return ResponseResult.success();
    }

}
