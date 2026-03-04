package org.wm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 功能描述：OAuth2授权服务启动类
 *
 * @author dove
 * @date 2023/07/31 16:36
 * @since 1.0
 **/
@EnableFeignClients
@SpringBootApplication
@MapperScan("org.wm.mapper")
public class OAuth2ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2ServerApplication.class, args);
    }

}
