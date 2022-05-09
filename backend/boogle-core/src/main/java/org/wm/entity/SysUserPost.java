package org.wm.entity;


import lombok.Data;

/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 用户和岗位关联 sys_user_post
 */
@Data
public class SysUserPost {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;

}
