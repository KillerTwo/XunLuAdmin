package org.wm.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 路由配置信息
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterReactVo {
    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private boolean hideInMenu;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private String redirect;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 图标名称（antd icon名称）
     */
    private String iconName;

    /**
     * 子路由
     */
    private List<RouterReactVo> routes;

}
