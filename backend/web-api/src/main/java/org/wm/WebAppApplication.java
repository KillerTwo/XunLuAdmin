package org.wm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.wm.properties.AppProperties;

@EnableConfigurationProperties(value = {AppProperties.class})
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class WebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }

}
