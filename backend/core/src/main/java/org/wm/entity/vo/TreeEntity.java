package org.wm.entity.vo;

import lombok.Data;
import org.wm.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 Tree基类
 */
@Data
public class TreeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 子部门
     */
    private List<?> children = new ArrayList<>();

}
