package org.wm.domain;


import lombok.Data;
import org.wm.entity.BaseEntity;


/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 字典类型表 sys_dict_type
 */
@Data
public class SysDictType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    private Long dictId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

}
