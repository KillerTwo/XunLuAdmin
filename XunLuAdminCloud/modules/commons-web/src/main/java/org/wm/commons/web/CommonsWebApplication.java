package org.wm.commons.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/07/16 20:11
 * @since 1.0
**/

@SpringBootApplication(scanBasePackages = {"org.wm.*"})
public class CommonsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonsWebApplication.class, args);
    }
}
