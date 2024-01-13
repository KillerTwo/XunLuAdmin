package org.wm.generator.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能描述：<功能描述>
 * 菜单
 * @author dove 
 * @date 2024/01/13 13:04
 * @since 1.0
**/
@Data
public class SysMenu {

    private Long id;


    private String name;

    private String path;

    private String component;

    private String redirect;

    private Meta meta;

    private Long parentId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Data
    public static class Meta {
        private Boolean hideMenu;

        private Boolean hideBreadcrumb;

        private String title;

        private String currentActiveMenu;

        private String icon;
    }

}
