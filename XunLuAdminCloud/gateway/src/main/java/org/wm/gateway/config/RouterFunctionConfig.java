package org.wm.gateway.config;

import com.alibaba.nacos.shaded.io.grpc.Server;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterFunctionConfig {

    /**
     *  请求/testFun时，响应为hello world的字符串
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> testFunRouterFunction() {
        RouterFunction<ServerResponse> router = RouterFunctions.route(RequestPredicates.path("/testFun"),
                request -> ServerResponse.ok().body(BodyInserters.fromObject("Hello world.")));
        return router;
    }

    /**
     *  请求路径为/image/webp时，将请求转发到http://httpbin.org,并在响应头中添加X-AnotherHeader
     * @param uilder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder uilder) {
        return uilder.routes()
                .route(r -> r.path("/image/webp")
                        .filters(f -> f.addResponseHeader("X-AnotherHeader", "baz"))
                        .uri("http://httpbin.org")
                )
                .build();
    }

}
