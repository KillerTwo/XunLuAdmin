package org.wm.gateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import org.wm.commons.constants.HttpStatus;
import org.wm.commons.constants.SecurityConstants;

import org.wm.gateway.utils.WebFluxServletUtils;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * 功能描述：<功能描述>
 * 全局过滤器，统一身份鉴权
 *
 * @author dove
 * @date 2023/07/11 23:14
 * @since 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        // 内部请求来源参数清除() 防止通过浏览器访问到内部接口
        removeHeader(mutate);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }


    @Override
    public int getOrder() {
        return 0;
    }


    /**
     * 功能描述：<功能描述>
     * 未鉴权响应
     *
     * @param exchange ServerWebExchange
     * @param msg      提示信息
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author dove
     * @date 2023/7/12 00:35
     */
    private Mono<Void> unAuthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]， 请求路径：{}", exchange.getRequest().getPath());

        return WebFluxServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }


    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate) {
        mutate.headers(httpHeaders -> httpHeaders.remove(SecurityConstants.FROM_SOURCE)).build();
    }

    /**
     * 内容编码
     * @param str String
     * @return String
     */
    public String urlEncode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }
}
