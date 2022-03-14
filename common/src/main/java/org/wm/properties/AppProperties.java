package org.wm.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @创建人 sk
 * @创建时间 2022/3/13
 * @描述
 */
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppProperties {


}
