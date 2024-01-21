package org.wm.auth.service;

import org.wm.commons.exception.ServiceException;
import org.wm.token.domain.TokenPayload;

import java.util.Map;

/**
 * 功能描述：<功能描述>
 *     认证service
 *
 * @author dove 
 * @date 2023/07/16 17:28
 * @since 1.0
**/
@Deprecated
public interface AuthService {



    /**
     * 功能描述：<功能描述>
     *     用户名密码登录
     *
     * @author dove
     * @date 2023/7/20 00:13
     * @param username  用户名
     * @param password  密码
     * @param code  验证码
     * @param uuid  验证码存储的uuid
     * @return TokenPayload jwt token信息
     * @throws ServiceException 用户验证失败
     */
    TokenPayload login(String username, String password, String code, String uuid);


    /**
     * 功能描述：<功能描述>
     *       获取登录验证码
     * @author dove
     * @date 2023/7/24 23:19
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> captcha();


}
