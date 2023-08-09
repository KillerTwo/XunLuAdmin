package org.wm.token.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 功能描述：<功能描述>
 *     token 格式
 *
 * @author dove 
 * @date 2023/07/25 22:54
 * @since 1.0
**/
@Data
@Builder
public class TokenPayload {

    /**
     *  token
     */
    private String accessToken;

    /**
     *  刷新token
     */
    private String refreshToken;

    /**
     *  过期时间
     */
    private Long expireTime;


    /**
     *  token 类型
     */
    private String type = "Bearer";

}
