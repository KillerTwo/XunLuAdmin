package org.wm.auth.params;

import lombok.Data;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/07/21 00:37
 * @since 1.0
**/
@Data
public class LoginParam {


    /** 用户名 */
    private String username;


    /** 密码 */
    private String password;

    /** 验证码 */
    private String code;

    /** 验证码关联uuid */
    private String uuid;

}
