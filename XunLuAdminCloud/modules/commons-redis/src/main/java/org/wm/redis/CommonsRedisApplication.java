package org.wm.redis;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/07/20 00:58
 * @since 1.0
**/
@SpringBootApplication(scanBasePackages = "org.wm.*")
public class CommonsRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonsRedisApplication.class, args);
    }
}
