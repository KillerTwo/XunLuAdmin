package org.wm.authentication.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * 功能描述：OAuth2端点工具类
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 **/
public final class OAuth2EndpointUtils {

    private OAuth2EndpointUtils() {
    }

    /**
     * 从请求中提取参数
     */
    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    parameters.add(key, value);
                }
            }
        });
        return parameters;
    }
}
