package org.wm.auth.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.wm.commons.constants.ServiceNameConstants;

/**
 * 功能描述：<功能描述>
 *     配置类
 *
 * @author dove 
 * @date 2023/07/19 23:21
 * @since 1.0
**/
@FeignClient(contextId = "ConfigInfoService", value = ServiceNameConstants.SYSTEM_SERVICE)
public interface ConfigServiceClient {



}
