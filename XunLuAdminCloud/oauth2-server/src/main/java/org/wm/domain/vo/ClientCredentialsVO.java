package org.wm.domain.vo;

import lombok.Data;

/**
 * 功能描述：客户端凭证VO
 * 用于返回新注册客户端的凭证信息
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 **/
@Data
public class ClientCredentialsVO {

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端密钥（仅在创建时返回，请妥善保管）
     */
    private String clientSecret;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 重定向URI列表
     */
    private String redirectUris;

    /**
     * 授权范围
     */
    private String scopes;

    /**
     * 提示信息
     */
    private String message = "请妥善保管client_secret，系统不会再次显示完整密钥";
}
