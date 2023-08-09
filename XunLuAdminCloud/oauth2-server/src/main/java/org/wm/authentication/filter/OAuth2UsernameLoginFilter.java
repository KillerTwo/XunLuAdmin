package org.wm.authentication.filter;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


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


    // private SysLoginService sysLoginService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var codeUuid = request.getParameter("uuid");
        var code = request.getParameter("code");
        try {
            // sysLoginService.validateCaptcha(this.obtainUsername(request), code, codeUuid);
        } catch (Exception e) {
            throw new UsernameNotFoundException("validate code error");
        }
        return super.attemptAuthentication(request, response);
    }
}
