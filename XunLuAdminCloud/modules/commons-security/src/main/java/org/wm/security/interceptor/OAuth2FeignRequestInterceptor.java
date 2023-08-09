package org.wm.security.interceptor;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;
import org.wm.commons.constants.SecurityConstants;
import org.wm.commons.web.utils.ServletUtils;

/**
 * 功能描述：<功能描述>
 *     处理Header Token 传递
 *
 * @author dove 
 * @date 2023/08/02 23:15
 * @since 1.0
**/
@Component
@RequiredArgsConstructor
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final BearerTokenResolver tokenResolver;


    @Override
    public void apply(RequestTemplate requestTemplate) {
        var formHeader = requestTemplate.headers().get(SecurityConstants.FROM_SOURCE);

        if (!formHeader.isEmpty() && formHeader.contains(SecurityConstants.INNER)) {
            return;
        }
        var httpServletRequest = ServletUtils.getRequest();

        // 避免请求参数的 query token 无法传递
        String token = tokenResolver.resolve(httpServletRequest);
        if (StrUtil.isBlank(token)) {
            return;
        }
        requestTemplate.header(HttpHeaders.AUTHORIZATION,
                String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), token));
    }
}
