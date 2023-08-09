package org.wm.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;
import org.wm.mapper.Oauth2RegisteredClientMapper;
import org.wm.domain.Oauth2RegisteredClient;
import org.wm.service.IOauth2RegisteredClientService;

/**
 * 授权服务器客户端注册Service业务层处理
 * 
 * @author wm
 * @date 2022-06-05
 */
@RequiredArgsConstructor
@Service
public class Oauth2RegisteredClientServiceImpl implements IOauth2RegisteredClientService {

    private final  Oauth2RegisteredClientMapper oauth2RegisteredClientMapper;

    private final RegisteredClientRepository jdbcRegisteredClientRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 查询授权服务器客户端注册
     * 
     * @param id 授权服务器客户端注册主键
     * @return 授权服务器客户端注册
     */
    @Override
    public Oauth2RegisteredClient selectOauth2RegisteredClientById(String id) {
        return oauth2RegisteredClientMapper.selectOauth2RegisteredClientById(id);
    }

    /**
     * 查询授权服务器客户端注册列表
     * 
     * @param oauth2RegisteredClient 授权服务器客户端注册
     * @return 授权服务器客户端注册
     */
    @Override
    public PageInfo<Oauth2RegisteredClient> selectOauth2RegisteredClientList(Oauth2RegisteredClient oauth2RegisteredClient) {
        return oauth2RegisteredClientMapper.selectOauth2RegisteredClientList(oauth2RegisteredClient);
    }


    public AuthorizationGrantType toAuthorizationGrantType(String grantType) {
        switch (grantType) {
            case "refresh_token" -> {
                return AuthorizationGrantType.REFRESH_TOKEN;
            }
            case "client_credentials" -> {
                return AuthorizationGrantType.CLIENT_CREDENTIALS;
            }
            default -> {
                return AuthorizationGrantType.AUTHORIZATION_CODE;
            }
        }
    }

    /**
     * 新增授权服务器客户端注册
     * 
     * @param oauth2RegisteredClient 授权服务器客户端注册
     * @return 结果
     */
    @Override
    public int insertOauth2RegisteredClient(Oauth2RegisteredClient oauth2RegisteredClient) {

        // var grantTypes = List.of(oauth2RegisteredClient.getAuthorizationGrantTypes());
        var grantTypes = Arrays.stream(oauth2RegisteredClient.getAuthorizationGrantTypes().split(",")).toList();

        var grantTypeList = grantTypes.stream().map(ele -> toAuthorizationGrantType(ele)).toList();

        var scopeList = Arrays.stream(oauth2RegisteredClient.getScopes().split(",")).toList();

        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(oauth2RegisteredClient.getClientId())
                // .clientSecret("secret")
                .clientSecret(passwordEncoder.encode(oauth2RegisteredClient.getClientSecret()))
                // .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantTypes(c -> c.addAll(grantTypeList))
                /*.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)*/
                // .redirectUri("http://127.0.0.1:8080")
                // .redirectUri("http://127.0.0.1:8080/authorized")
                .redirectUri(oauth2RegisteredClient.getRedirectUris())
                .scope(OidcScopes.OPENID)
                .scopes(c -> c.addAll(scopeList))
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        jdbcRegisteredClientRepository.save(registeredClient);
        return 1;
    }

    /**
     * 修改授权服务器客户端注册
     * 
     * @param oauth2RegisteredClient 授权服务器客户端注册
     * @return 结果
     */
    @Override
    public int updateOauth2RegisteredClient(Oauth2RegisteredClient oauth2RegisteredClient) {
        return oauth2RegisteredClientMapper.updateOauth2RegisteredClient(oauth2RegisteredClient);
    }

    /**
     * 批量删除授权服务器客户端注册
     * 
     * @param ids 需要删除的授权服务器客户端注册主键
     * @return 结果
     */
    @Override
    public int deleteOauth2RegisteredClientByIds(String[] ids) {
        return oauth2RegisteredClientMapper.deleteOauth2RegisteredClientByIds(ids);
    }

    /**
     * 删除授权服务器客户端注册信息
     * 
     * @param id 授权服务器客户端注册主键
     * @return 结果
     */
    @Override
    public int deleteOauth2RegisteredClientById(String id) {
        return oauth2RegisteredClientMapper.deleteOauth2RegisteredClientById(id);
    }
}
