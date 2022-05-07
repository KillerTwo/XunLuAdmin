package org.wm.config.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于获取自定义线程池
 * @author wm
 * @date 2022年01月25日22:28:18
 */
public class ThreadPoolExecutorUtil {

    public static ThreadPoolExecutor getPoll(){
        return new ThreadPoolExecutor(
                AsyncTaskProperties.corePoolSize,
                AsyncTaskProperties.maxPoolSize,
                AsyncTaskProperties.keepAliveSeconds,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(AsyncTaskProperties.queueCapacity),
                new TheadFactoryName()
        );
    }
}
