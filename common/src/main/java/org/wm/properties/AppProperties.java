package org.wm.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @创建人 sk
 * @创建时间 2022/3/13
 * @描述
 */
@Data
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppProperties {

    private String captchaType;



}
