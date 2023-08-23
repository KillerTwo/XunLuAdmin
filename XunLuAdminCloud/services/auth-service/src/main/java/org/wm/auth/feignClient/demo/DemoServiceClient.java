package org.wm.auth.feignClient.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wm.commons.constants.ServiceNameConstants;
import org.wm.commons.response.ResponseResult;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/07/30 01:00
 * @since 1.0
**/
@FeignClient(contextId = "DemoService", value = ServiceNameConstants.DEMO_SERVICE, fallbackFactory = DemoServiceClientFallback.class)
public interface DemoServiceClient {


    @RequestMapping(method = RequestMethod.GET, value = "/hello", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseResult<?> helloDemo();


    @RequestMapping(method = RequestMethod.GET, value = "/hello1", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseResult<?> helloDemo1();
}
