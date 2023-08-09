package org.wm.token.jks.jwt;

import java.util.Map;

/**
 * 功能描述：<功能描述>
 *     token 操作工具
 *
 * @author dove 
 * @date 2023/07/15 19:42
 * @since 1.0
**/
public interface TokenGenerator<T> {

    /**
     * 功能描述：<功能描述>
     *
     * @author dove
     * @date 2023/7/15 19:49
     * @param claims
     * @return java.lang.String
     * @throws
     */
    String createToken(Map<String, Object> claims);


    /**
     * 功能描述：<功能描述>
     *
     * @author dove
     * @date 2023/7/15 19:49
     * @param claims
     * @return java.lang.String
     * @throws
     */
    String createToken(T claims);


    /**
     * 功能描述：<功能描述>
     *
     * @author dove
     * @date 2023/7/15 19:50
 * @param token
     * @return T
     * @throws
     */
    T parseToken(String token);


    /**
     * 功能描述：<功能描述>
     *       获取jwt载体中的key对应的value
     * @author dove
     * @date 2023/7/15 20:05
     * @param claims  jwt claims
     * @param key  key
     * @return java.lang.Object
     */
    Object getValue(T claims, String key);

}
