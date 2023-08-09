package org.wm.gateway.utils;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.wm.commons.response.ResponseResult;
import org.wm.commons.utils.ObjectMapperUtil;
import reactor.core.publisher.Mono;

/**
 * 功能描述：<功能描述>
 *     webflux工具类
 *
 * @author dove 
 * @date 2023/07/30 17:20
 * @since 1.0
**/
public class WebFluxServletUtils {


    /**
     * 功能描述：<功能描述>
     * 返回错误响应
     *
     * @param response ServerHttpResponse
     * @param value    响应内容
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author dove
     * @date 2023/7/12 00:33
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value) {
        return webFluxResponseWriter(response, HttpStatus.OK, value, 500);
    }

    /**
     * 功能描述：<功能描述>
     * 成功响应JSON格式数据
     *
     * @param response ServerHttpResponse
     * @param value    响应值
     * @param code     响应码
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author dove
     * @date 2023/7/12 00:32
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value, int code) {
        return webFluxResponseWriter(response, HttpStatus.OK, value, code);
    }


    /**
     * 功能描述：<功能描述>
     * 向Response写入JSON格式的数据
     *
     * @param response ServerHttpResponse
     * @param status   Http状态码
     * @param value    响应值
     * @param code     响应码
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author dove
     * @date 2023/7/12 00:30
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus status,
                                                   Object value, int code) {
        return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, value, code);
    }


    /**
     * 功能描述：<功能描述>
     * 渲染响应值， 向HttpResponse写入响应数据
     *
     * @param response    ServerHttpResponse
     * @param contentType content-type
     * @param status      http状态码
     * @param value       响应值
     * @param code        响应码
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author dove
     * @date 2023/7/12 00:24
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus status,
                                                   Object value, int code) {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        ResponseResult<?> result = ResponseResult.error(code, value.toString());
        DataBuffer dataBuffer = response.bufferFactory().wrap(ObjectMapperUtil.writeValueAsString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

}
