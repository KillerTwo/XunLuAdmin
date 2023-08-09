package org.wm.commons.config.thread;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池配置属性类
 * @author wm
 * @date 2022年01月25日22:28:18
 */
@Data
@ConfigurationProperties(prefix = "task.pool")
@Component
public class AsyncTaskProperties {

    public static int corePoolSize = 10;

    public static int maxPoolSize = 10;

    public static int keepAliveSeconds = 60;

    public static int queueCapacity = 10;
}
