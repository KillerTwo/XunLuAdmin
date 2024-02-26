package org.wm.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/08/02 22:50
 * @since 1.0
 *
**/
@EnableFeignClients   // TODO 需要验证这个工具类使用@SpringBootApplication是否合适
@SpringBootApplication
public class SecurityCommonsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityCommonsApplication.class, args);
    }

}
