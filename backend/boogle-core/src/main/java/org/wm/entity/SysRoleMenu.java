package org.wm.entity;

import lombok.Data;

/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 角色和菜单关联 sys_role_menu
 */
@Data
public class SysRoleMenu {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
