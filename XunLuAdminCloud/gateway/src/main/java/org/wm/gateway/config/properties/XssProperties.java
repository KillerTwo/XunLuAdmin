package org.wm.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：<功能描述>
 *     xss配置属性
 *
 * @author dove 
 * @date 2023/07/30 17:13
 * @since 1.0
**/
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "security.xss")
public class XssProperties {

    /**
     * Xss开关
     */
    private Boolean enabled;

    /**
     * 排除路径
     */
    private List<String> excludeUrls = new ArrayList<>();

}
