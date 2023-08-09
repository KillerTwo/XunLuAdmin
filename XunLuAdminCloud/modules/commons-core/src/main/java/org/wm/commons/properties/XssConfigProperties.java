package org.wm.commons.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "xss")
@Component
public class XssConfigProperties {

    private String excludes = "";


    private String urlPatterns = "";
}
