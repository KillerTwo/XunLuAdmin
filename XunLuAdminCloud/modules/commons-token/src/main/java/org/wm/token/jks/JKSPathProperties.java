package org.wm.token.jks;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jks")
public class JKSPathProperties {

    private String path = "jks/cert.jks";

    private String password = "123456";

    private String alias = "jwt-alias-name";

}
