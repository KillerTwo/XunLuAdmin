package org.wm.auth.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wm.commons.constants.SecurityConstants;
import org.wm.commons.constants.ServiceNameConstants;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.response.ResponseResult;

/**
 * 功能描述：<功能描述>
 *     调用用户服务
 *
 * @author dove 
 * @date 2023/07/17 00:20
 * @since 1.0
**/
@FeignClient(contextId = "UserInfoService", value = ServiceNameConstants.SYSTEM_SERVICE)
public interface UserServiceClient {



    /**
     * 功能描述：<功能描述>
     *     获取用户登录信息
     *
     * @author dove
     * @date 2023/7/19 23:59
     * @param username  用户名
     * @return org.wm.commons.response.ResponseResult<org.wm.commons.dto.LoginUser>
     */
    @RequestMapping(method = RequestMethod.GET, value = "/system/user/userinfo/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = {SecurityConstants.FROM_SOURCE + "=" +SecurityConstants.INNER})
    ResponseResult<LoginUser> userInfoByUsername(@PathVariable("username") String username);



}
