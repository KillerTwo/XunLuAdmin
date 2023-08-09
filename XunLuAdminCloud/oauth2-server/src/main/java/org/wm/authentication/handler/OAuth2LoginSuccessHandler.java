package org.wm.authentication.handler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 功能描述：<功能描述>
 *     登录成功处理器
 *
 * @author dove
 * @date 2023/7/31 16:29
 * @since 1.0
 **/
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // recordLoginInfo(loginUser.getUserId());
        // 生成token
        // tokenService.refreshToken(loginUser);
        // var token = tokenService.createToken(loginUser);
        // super.onAuthenticationSuccess(request, response, chain, authentication);
        // clearAuthenticationAttributes(request);
        // Use the DefaultSavedRequest URL
        // String targetUrl = savedRequest.getRedirectUrl();
        /*String targetUrl = "";
        getRedirectStrategy().sendRedirect(request, response, targetUrl);*/
        super.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
