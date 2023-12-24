package org.wm.auth.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wm.commons.constants.ServiceNameConstants;
import org.wm.commons.response.ResponseResult;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/07/17 01:42
 * @since 1.0
**/
@Component
@FeignClient(contextId = "MenuInfoService", value = ServiceNameConstants.SYSTEM_SERVICE)
public interface MenuServiceClient {


    @RequestMapping(method = RequestMethod.GET, value = "/system/menu/{menuId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<?> getMenuInfo(@PathVariable("menuId") Long menuId);


}
