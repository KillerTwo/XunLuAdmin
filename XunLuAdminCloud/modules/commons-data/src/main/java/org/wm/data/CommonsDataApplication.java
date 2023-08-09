package org.wm.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/07/21 00:30
 * @since 1.0
**/
@SpringBootApplication(scanBasePackages = "org.wm.*")
public class CommonsDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonsDataApplication.class, args);
    }

}
