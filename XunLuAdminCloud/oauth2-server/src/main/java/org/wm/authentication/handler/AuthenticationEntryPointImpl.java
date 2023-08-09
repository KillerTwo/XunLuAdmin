package org.wm.authentication.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.wm.commons.response.ResponseResult;
import org.wm.commons.utils.ObjectMapperUtil;
import org.wm.commons.utils.StringUtils;
import org.wm.commons.web.utils.ServletUtils;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;


/**
 * 认证失败处理类 返回未授权
 *
 * @author ruoyi
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    @Serial
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        int code = HttpStatus.UNAUTHORIZED.value();
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源 Exception: {}", request.getRequestURI(), e.getMessage());
        ServletUtils.renderString(response, ObjectMapperUtil.objectMapper().writeValueAsString(ResponseResult.error(code, msg)));
    }
}
