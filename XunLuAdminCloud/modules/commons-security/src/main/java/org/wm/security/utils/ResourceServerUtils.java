package org.wm.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.wm.commons.constants.ServiceNameConstants;

import java.util.Random;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2024/03/05 22:34
 * @since 1.0
**/
@Slf4j
public class ResourceServerUtils {

    public static String serverUrl(DiscoveryClient discoveryClient) {

        var instances = discoveryClient.getInstances(ServiceNameConstants.OAUTH2_SERVICE);

        var issuer = "";
        if (!instances.isEmpty()) {
            // var instance = instances.get(0);
            // 随机选择一个
            var instance = instances.get(new Random().nextInt(instances.size()));

            issuer = instance.getUri().toString();  // String.format("http://%s", instance.toInetAddr());
            log.info("issuer: {}", issuer);
        } else {
            log.error("找不到服务{}, {}", issuer, ServiceNameConstants.OAUTH2_SERVICE);
        }
        return issuer;
    }


}
