package org.wm.authentication.filter;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wm.feignClient.ValidateCodeClient;


/**
 * 功能描述：<功能描述>
 *     用户名密码登录拦截器
 *
 * @author dove
 * @date 2023/8/2 23:59
 * @since 1.0
 **/
@RequiredArgsConstructor
public class OAuth2UsernameLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final static Logger logger = LoggerFactory.getLogger(OAuth2UsernameLoginFilter.class);

    // private SysLoginService sysLoginService;

    // private final ValidateCodeClient validateCodeClient;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /*var codeUuid = request.getParameter("uuid");
        var code = request.getParameter("code");
        try {
            // sysLoginService.validateCaptcha(this.obtainUsername(request), code, codeUuid);
            var res = validateCodeClient.validateCode(codeUuid, code);

            if (!res.getData()) {
                logger.warn("验证码错误");
                throw new UsernameNotFoundException("validate code error");
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("validate code error");
        }*/
        return super.attemptAuthentication(request, response);
    }
}
