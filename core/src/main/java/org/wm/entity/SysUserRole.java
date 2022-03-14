package org.wm.entity;

import lombok.Data;

/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 用户和角色关联 sys_user_role
 */
@Data
public class SysUserRole {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
