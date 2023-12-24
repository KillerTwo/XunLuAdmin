package org.wm.generator.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 读取代码生成相关配置
 */

@Component
@ConfigurationProperties(prefix = "gen")
@RefreshScope
public class GenConfig {
    /**
     * 作者
     */
    @Getter
    public static String author;

    /**
     * 生成包路径
     */
    @Getter
    public static String packageName;

    /**
     * 自动去除表前缀，默认是false
     */
    public static boolean autoRemovePre;

    /**
     * 表前缀(类名不会包含表前缀)
     */
    @Getter
    public static String tablePrefix;

    // @Value("${gen.author}")
    public void setAuthor(String author) {
        GenConfig.author = author;
    }

    // @Value("${gen.packageName}")
    public void setPackageName(String packageName) {
        GenConfig.packageName = packageName;
    }

    public static boolean getAutoRemovePre() {
        return autoRemovePre;
    }

     // @Value("${gen.autoRemovePre}")
    public void setAutoRemovePre(boolean autoRemovePre) {
        GenConfig.autoRemovePre = autoRemovePre;
    }

    // @Value("${gen.tablePrefix}")
    public void setTablePrefix(String tablePrefix) {
        GenConfig.tablePrefix = tablePrefix;
    }
}
