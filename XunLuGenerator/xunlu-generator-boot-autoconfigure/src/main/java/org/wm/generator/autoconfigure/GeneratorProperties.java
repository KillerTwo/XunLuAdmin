package org.wm.generator.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * spring boot starter properties
 *
 * @author eumenides
 * 
 */
@Data
@ConfigurationProperties("generator")
public class GeneratorProperties {
    /**
     * 模板路径
     */
    private String template = "/template/";
}
