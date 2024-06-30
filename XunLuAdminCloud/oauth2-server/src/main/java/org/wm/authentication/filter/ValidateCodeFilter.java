package org.wm.authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.wm.commons.response.ResponseResult;
import org.wm.commons.utils.ObjectMapperUtil;
import org.wm.commons.utils.StringUtils;
import org.wm.commons.web.utils.ServletUtils;
import org.wm.feignClient.ValidateCodeClient;

import java.io.IOException;

/**
 * 功能描述：<功能描述>
 * 验证码验证拦截器
 * @author dove 
 * @date 2024/06/30 17:43
 * @since 1.0
**/
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(OAuth2UsernameLoginFilter.class);


    private final ValidateCodeClient validateCodeClient;

    /** 登录类型是password的登录 **/
    private final static String OAUTH2_LOGIN_URL = "/oauth2/token";

    /** 内部用户名密码登录 */
    private final static String USERNAME_LOGIN_URL = "/oauth2/doLogin";

    private final static String GRANT_TYPE = "password";

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var grantType = request.getParameter("grant_type");
        if ((pathMatcher.match(OAUTH2_LOGIN_URL, request.getRequestURI()) && GRANT_TYPE.equals(grantType))
                || pathMatcher.match(USERNAME_LOGIN_URL, request.getRequestURI())) {
            var codeUuid = request.getParameter("uuid");
            var code = request.getParameter("code");
            if (StringUtils.isEmpty(codeUuid) || StringUtils.isEmpty(code)) {
                logger.warn("codeUuid和code is null");
                writeResponse(request, response, "Captcha code is invalid");
                return;
            }
            try {
                // sysLoginService.validateCaptcha(this.obtainUsername(request), code, codeUuid);
                var res = validateCodeClient.validateCode(codeUuid, code);

                if (!res.getData()) {
                    logger.warn("验证码错误");
                    writeResponse(request, response, "Captcha code is invalid");
                    return;
                }
            } catch (Exception e) {
                logger.error("调用验证码服务出错", e);
                writeResponse(request, response, e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void writeResponse(HttpServletRequest request, HttpServletResponse response, String error) throws IOException {
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源 Exception: {}", request.getRequestURI(), error);
        ServletUtils.renderString(response, ObjectMapperUtil.objectMapper().writeValueAsString(ResponseResult.error(401, msg)));
    }
}
