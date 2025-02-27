package org.wm.system.entity;

import lombok.Data;
import org.wm.commons.dto.BaseEntity;

import java.io.Serial;

/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述
 */
@Data
public class SysConfig extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    private Long configId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;
}
