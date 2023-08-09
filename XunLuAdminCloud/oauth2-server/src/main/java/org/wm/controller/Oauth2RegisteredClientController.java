package org.wm.controller;

import java.util.Arrays;
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
 * 授权服务器客户端注册Controller
 *
 * @author wm
 * @date 2022-06-05
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2/registeredClient")
public class Oauth2RegisteredClientController extends BaseController {

    private final IOauth2RegisteredClientService oauth2RegisteredClientService;

    /**
     * 查询授权服务器客户端注册列表
     */
    @GetMapping("/list")
    public PageResult<Oauth2RegisteredClientDto> list(Oauth2RegisteredClient oauth2RegisteredClient) {
        PageInfo<Oauth2RegisteredClient> list = oauth2RegisteredClientService.selectOauth2RegisteredClientList(oauth2RegisteredClient);
        var resList = list.getList().stream().map(ele -> {
            Oauth2RegisteredClientDto oauth2RegisteredClientdto = new Oauth2RegisteredClientDto();
            oauth2RegisteredClientdto.setClientId(ele.getClientId());
            oauth2RegisteredClientdto.setClientSecret(ele.getClientSecret());
            oauth2RegisteredClientdto.setClientName(ele.getClientName());
            oauth2RegisteredClientdto.setAuthorizationGrantTypes(Arrays.stream(ele.getAuthorizationGrantTypes().split(",")).toList());
            oauth2RegisteredClientdto.setScopes(Arrays.stream(ele.getScopes().split(",")).toList());
            oauth2RegisteredClientdto.setRedirectUris(ele.getRedirectUris());
            return oauth2RegisteredClientdto;
        }).toList();
        return PageResult.success(resList, list.getTotal());
    }

    /**
     * 获取授权服务器客户端注册详细信息
     */
    @GetMapping(value = "/{id}")
    public ResponseResult<Oauth2RegisteredClient> getInfo(@PathVariable("id") String id) {
        return ResponseResult.success(oauth2RegisteredClientService.selectOauth2RegisteredClientById(id));
    }

    /**
     * 新增授权服务器客户端注册
     */
    @Log(title = "授权服务器客户端注册", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseResult<?> add(@RequestBody Oauth2RegisteredClientDto oauth2RegisteredClientDto) {
        Oauth2RegisteredClient oauth2RegisteredClient = new Oauth2RegisteredClient();
        oauth2RegisteredClient.setClientId(oauth2RegisteredClientDto.getClientId());
        oauth2RegisteredClient.setClientSecret(oauth2RegisteredClientDto.getClientSecret());
        oauth2RegisteredClient.setClientName(oauth2RegisteredClientDto.getClientName());
        oauth2RegisteredClient.setAuthorizationGrantTypes(oauth2RegisteredClientDto.getAuthorizationGrantTypes().stream().collect(Collectors.joining(",")));
        oauth2RegisteredClient.setScopes(oauth2RegisteredClientDto.getScopes().stream().collect(Collectors.joining(",")));
        oauth2RegisteredClient.setRedirectUris(oauth2RegisteredClientDto.getRedirectUris());
        return toAjax(oauth2RegisteredClientService.insertOauth2RegisteredClient(oauth2RegisteredClient));
    }

    /**
     * 修改授权服务器客户端注册
     */
    @Log(title = "授权服务器客户端注册", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseResult<?> edit(@RequestBody Oauth2RegisteredClient oauth2RegisteredClient) {
        return toAjax(oauth2RegisteredClientService.updateOauth2RegisteredClient(oauth2RegisteredClient));
    }

    /**
     * 删除授权服务器客户端注册
     */
    @Log(title = "授权服务器客户端注册", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public ResponseResult<?> remove(@PathVariable String[] ids) {
        return toAjax(oauth2RegisteredClientService.deleteOauth2RegisteredClientByIds(ids));
    }
}
