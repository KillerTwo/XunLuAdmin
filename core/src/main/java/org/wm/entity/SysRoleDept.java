package org.wm.entity;


import lombok.Data;

/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 角色和部门关联 sys_role_dept
 */
@Data
public class SysRoleDept {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;
}
