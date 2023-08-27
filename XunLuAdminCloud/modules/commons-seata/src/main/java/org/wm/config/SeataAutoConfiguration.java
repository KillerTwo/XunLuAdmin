package org.wm.config;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.wm.commons.properties.factory.YamlPropertySourceFactory;

/**
 * 功能描述：<功能描述>
 *     Seata 分布式事务自动配置类
 *
 * @author dove 
 * @date 2023/08/27 15:24
 * @since 1.0
**/


@PropertySource(value = "classpath:seata-config.yml", factory = YamlPropertySourceFactory.class)
@EnableAutoDataSourceProxy(useJdkProxy = true)
@Configuration(proxyBeanMethods = false)
public class SeataAutoConfiguration {



}
