package org.wm.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wm.commons.constants.SecurityConstants;
import org.wm.commons.constants.ServiceNameConstants;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.response.ResponseResult;

/**
 * 功能描述：<功能描述>
 * 验证码服务
 * @author dove 
 * @date 2024/06/30 17:10
 * @since 1.0
**/
@FeignClient(contextId = "ValidatorService", value = ServiceNameConstants.VALIDATOR_SERVICE)
public interface ValidateCodeClient {

    /**
     * 功能描述：<功能描述>
     *       验证用户提交的验证码是否正确
     * @author dove
     * @date 2024/6/30 17:13
     * @param uuid 验证码对应的标识
     * @param code 验证码
     * @return org.wm.commons.response.ResponseResult<java.lang.Boolean>
     * @throws
     */
    @RequestMapping(method = RequestMethod.GET, value = "/validator/validateCaptcha/{uuid}/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = {SecurityConstants.FROM_SOURCE + "=" +SecurityConstants.INNER})
    ResponseResult<Boolean> validateCode(@PathVariable("uuid") String uuid, @PathVariable("code") String code);

}
