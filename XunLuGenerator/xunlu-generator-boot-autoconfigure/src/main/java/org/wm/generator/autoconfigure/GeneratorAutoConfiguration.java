package org.wm.generator.autoconfigure;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.wm.generator.config.template.GeneratorConfig;

/**
 * spring boot starter AutoConfiguration
 *
 * @author eumenides
 * 
 */
@Configuration
@AllArgsConstructor
@ComponentScan(basePackages = {"net.maku.generator"})
@EnableConfigurationProperties(GeneratorProperties.class)
public class GeneratorAutoConfiguration {
    private final GeneratorProperties properties;

    @Bean
    GeneratorConfig generatorConfig() {
        return new GeneratorConfig(properties.getTemplate());
    }

}
