package org.wm.enums;

/**
 * 功能描述：OAuth2客户端类型枚举
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 **/
public enum ClientType {

    /**
     * 第三方应用客户端 - 用于外部应用接入
     * 使用授权码模式 + PKCE
     */
    THIRD_PARTY_APP("third_party_app", "第三方应用"),

    /**
     * 内部管理后台客户端 - 用于本系统前端
     * 使用密码模式（仅限内部使用）
     */
    INTERNAL_ADMIN("internal_admin", "内部管理后台"),

    /**
     * 微服务客户端 - 用于服务间调用
     * 使用客户端凭证模式
     */
    MICROSERVICE("microservice", "微服务"),

    /**
     * 移动应用客户端 - 用于移动端APP
     * 使用授权码模式 + PKCE
     */
    MOBILE_APP("mobile_app", "移动应用");

    private final String code;
    private final String description;

    ClientType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
