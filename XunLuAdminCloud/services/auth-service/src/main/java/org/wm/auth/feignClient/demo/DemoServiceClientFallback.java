package org.wm.auth.feignClient.demo;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.wm.commons.response.ResponseResult;

import java.util.concurrent.TimeoutException;

/**
 * 功能描述：<功能描述>
 *     服务熔断降级
 *
 * @author dove 
 * @date 2023/08/16 23:35
 * @since 1.0
**/
@Component
public class DemoServiceClientFallback implements FallbackFactory<DemoServiceClient> {



    @Override
    public DemoServiceClient create(Throwable cause) {

        /*if (cause instanceof TimeoutException) {
            return new ServiceAClientWithTimeoutFallback();
        }
        if (cause instanceof IllegalArgumentException) {
            return new ServiceAClientWithIAEFallback();
        }*/
        System.err.println("DemoServiceClientFallback");

        return new DemoServiceClient() {

            @Override
            public ResponseResult<?> helloDemo() {
                return ResponseResult.success("服务熔断降级 hello world.");
            }

            @Override
            public ResponseResult<?> helloDemo1() {
                return null;
            }
        };
    }
}
