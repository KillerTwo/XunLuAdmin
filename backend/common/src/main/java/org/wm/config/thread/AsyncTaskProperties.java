package org.wm.config.thread;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 线程池配置属性类
 * @author wm
 * @date 2022年01月25日22:28:18
 */
@Data
@Component
public class AsyncTaskProperties {

    public static int corePoolSize;

    public static int maxPoolSize;

    public static int keepAliveSeconds;

    public static int queueCapacity;

    @Value("${task.pool.core-pool-size}")
    public void setCorePoolSize(int corePoolSize) {
        AsyncTaskProperties.corePoolSize = corePoolSize;
    }

    @Value("${task.pool.max-pool-size}")
    public void setMaxPoolSize(int maxPoolSize) {
        AsyncTaskProperties.maxPoolSize = maxPoolSize;
    }

    @Value("${task.pool.keep-alive-seconds}")
    public void setKeepAliveSeconds(int keepAliveSeconds) {
        AsyncTaskProperties.keepAliveSeconds = keepAliveSeconds;
    }

    @Value("${task.pool.queue-capacity}")
    public void setQueueCapacity(int queueCapacity) {
        AsyncTaskProperties.queueCapacity = queueCapacity;
    }
}
