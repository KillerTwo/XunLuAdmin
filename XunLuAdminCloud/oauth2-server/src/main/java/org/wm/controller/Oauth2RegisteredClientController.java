package org.wm.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.wm.commons.response.PageResult;
import org.wm.commons.response.ResponseResult;
import org.wm.commons.web.controller.BaseController;
import org.wm.domain.Oauth2RegisteredClient;
import org.wm.domain.dto.Oauth2RegisteredClientDto;
import org.wm.logging.annotation.Log;
import org.wm.logging.enums.BusinessType;
import org.wm.service.IOauth2RegisteredClientService;


/**
 * OAuth2客户端管理Controller
 * 参考GitHub OAuth Apps设计，提供完整的客户端应用管理功能
 *
 * @author dove
 * @date 2026/03/01
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2/registeredClient")
public class Oauth2RegisteredClientController extends BaseController {

    private final IOauth2RegisteredClientService oauth2RegisteredClientService;

    /**
     * 查询OAuth2客户端列表
     */
    @GetMapping("/list")
    public PageResult<Oauth2RegisteredClientDto> list(Oauth2RegisteredClient oauth2RegisteredClient) {
        PageInfo<Oauth2RegisteredClient> list = oauth2RegisteredClientService.selectOauth2RegisteredClientList(oauth2RegisteredClient);
        var resList = list.getList().stream().map(this::convertToDto).toList();
        return PageResult.success(resList, list.getTotal());
    }

    /**
     * 获取OAuth2客户端详细信息
     */
    @GetMapping(value = "/{id}")
    public ResponseResult<Oauth2RegisteredClientDto> getInfo(@PathVariable("id") String id) {
        Oauth2RegisteredClient client = oauth2RegisteredClientService.selectOauth2RegisteredClientById(id);
        return ResponseResult.success(convertToDto(client));
    }

    /**
     * 获取可用的授权类型和客户端认证方法
     * 用于前端下拉框选择
     */
    @GetMapping("/enums")
    public ResponseResult<Map<String, Object>> getEnums() {
        Map<String, Object> enums = new HashMap<>();
        enums.put("authorizationGrantTypes", Arrays.asList(
                "authorization_code",
                "client_credentials",
                "refresh_token",
                "password"
        ));
        enums.put("clientAuthenticationMethods", Arrays.asList(
                "client_secret_basic",
                "client_secret_post",
                "none"
        ));
        enums.put("scopes", Arrays.asList(
                "openid",
                "profile",
                "email",
                "all",
                "user.read",
                "user.write"
        ));
        return ResponseResult.success(enums);
    }

    /**
     * 新增OAuth2客户端
     */
    @Log(title = "OAuth2客户端管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseResult<?> add(@RequestBody Oauth2RegisteredClientDto oauth2RegisteredClientDto) {
        Oauth2RegisteredClient oauth2RegisteredClient = new Oauth2RegisteredClient();
        oauth2RegisteredClient.setClientId(oauth2RegisteredClientDto.getClientId());
        oauth2RegisteredClient.setClientSecret(oauth2RegisteredClientDto.getClientSecret());
        oauth2RegisteredClient.setClientName(oauth2RegisteredClientDto.getClientName());

        // 处理授权类型：数组转逗号分隔字符串
        if (oauth2RegisteredClientDto.getAuthorizationGrantTypes() != null && !oauth2RegisteredClientDto.getAuthorizationGrantTypes().isEmpty()) {
            oauth2RegisteredClient.setAuthorizationGrantTypes(
                    String.join(",", oauth2RegisteredClientDto.getAuthorizationGrantTypes())
            );
        }

        // 处理 Scopes：数组转逗号分隔字符串
        if (oauth2RegisteredClientDto.getScopes() != null && !oauth2RegisteredClientDto.getScopes().isEmpty()) {
            oauth2RegisteredClient.setScopes(
                    String.join(",", oauth2RegisteredClientDto.getScopes())
            );
        }

        // redirectUris 和 clientAuthenticationMethods 直接赋值（已经是字符串）
        oauth2RegisteredClient.setRedirectUris(oauth2RegisteredClientDto.getRedirectUris());
        oauth2RegisteredClient.setClientAuthenticationMethods(oauth2RegisteredClientDto.getClientAuthenticationMethods());

        return toAjax(oauth2RegisteredClientService.insertOauth2RegisteredClient(oauth2RegisteredClient));
    }

    /**
     * 修改OAuth2客户端
     */
    @Log(title = "OAuth2客户端管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseResult<?> edit(@RequestBody Oauth2RegisteredClientDto oauth2RegisteredClientDto) {
        Oauth2RegisteredClient oauth2RegisteredClient = new Oauth2RegisteredClient();
        oauth2RegisteredClient.setId(oauth2RegisteredClientDto.getId());
        oauth2RegisteredClient.setClientId(oauth2RegisteredClientDto.getClientId());
        oauth2RegisteredClient.setClientName(oauth2RegisteredClientDto.getClientName());

        // 处理授权类型：数组转逗号分隔字符串
        if (oauth2RegisteredClientDto.getAuthorizationGrantTypes() != null && !oauth2RegisteredClientDto.getAuthorizationGrantTypes().isEmpty()) {
            oauth2RegisteredClient.setAuthorizationGrantTypes(
                    String.join(",", oauth2RegisteredClientDto.getAuthorizationGrantTypes())
            );
        }

        // 处理 Scopes：数组转逗号分隔字符串
        if (oauth2RegisteredClientDto.getScopes() != null && !oauth2RegisteredClientDto.getScopes().isEmpty()) {
            oauth2RegisteredClient.setScopes(
                    String.join(",", oauth2RegisteredClientDto.getScopes())
            );
        }

        // redirectUris 和 clientAuthenticationMethods 直接赋值（已经是字符串）
        oauth2RegisteredClient.setRedirectUris(oauth2RegisteredClientDto.getRedirectUris());
        oauth2RegisteredClient.setClientAuthenticationMethods(oauth2RegisteredClientDto.getClientAuthenticationMethods());

        return toAjax(oauth2RegisteredClientService.updateOauth2RegisteredClient(oauth2RegisteredClient));
    }

    /**
     * 删除OAuth2客户端
     */
    @Log(title = "OAuth2客户端管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public ResponseResult<?> remove(@PathVariable String[] ids) {
        return toAjax(oauth2RegisteredClientService.deleteOauth2RegisteredClientByIds(ids));
    }

    /**
     * 实体转DTO
     */
    private Oauth2RegisteredClientDto convertToDto(Oauth2RegisteredClient client) {
        if (client == null) {
            return null;
        }

        Oauth2RegisteredClientDto dto = new Oauth2RegisteredClientDto();
        dto.setId(client.getId());
        dto.setClientId(client.getClientId());
        // 隐藏密钥，只显示是否配置
        dto.setClientSecret(client.getClientSecret() != null && !client.getClientSecret().isEmpty() ? "********" : null);
        dto.setClientName(client.getClientName());
        dto.setClientIdIssuedAt(client.getClientIdIssuedAt());
        dto.setClientAuthenticationMethods(client.getClientAuthenticationMethods());

        // 授权类型：逗号分隔字符串转数组
        if (client.getAuthorizationGrantTypes() != null && !client.getAuthorizationGrantTypes().isEmpty()) {
            dto.setAuthorizationGrantTypes(
                    Arrays.asList(client.getAuthorizationGrantTypes().split(","))
            );
        }

        // Scopes：逗号分隔字符串转数组
        if (client.getScopes() != null && !client.getScopes().isEmpty()) {
            dto.setScopes(
                    Arrays.asList(client.getScopes().split(","))
            );
        }

        // redirectUris：保持逗号分隔格式（前端会处理显示）
        dto.setRedirectUris(client.getRedirectUris());

        return dto;
    }
}
