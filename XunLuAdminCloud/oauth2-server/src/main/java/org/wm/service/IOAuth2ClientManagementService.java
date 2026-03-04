package org.wm.service;

import org.wm.domain.dto.Oauth2RegisteredClientDto;
import org.wm.domain.vo.ClientCredentialsVO;
import org.wm.domain.vo.RegisteredClientVO;

import java.util.List;

/**
 * 功能描述：OAuth2客户端管理服务接口
 * 用于第三方应用注册和管理
 *
 * @author dove
 * @date 2026/03/01
 * @since 1.0
 **/
public interface IOAuth2ClientManagementService {

    /**
     * 注册新的第三方应用客户端
     * @param dto 客户端信息
     * @return 客户端凭证（包含client_id和client_secret）
     */
    ClientCredentialsVO registerThirdPartyClient(Oauth2RegisteredClientDto dto);

    /**
     * 更新客户端信息
     * @param clientId 客户端ID
     * @param dto 更新的客户端信息
     * @return 是否成功
     */
    boolean updateClient(String clientId, Oauth2RegisteredClientDto dto);

    /**
     * 删除客户端
     * @param clientId 客户端ID
     * @return 是否成功
     */
    boolean deleteClient(String clientId);

    /**
     * 获取客户端列表
     * @return 客户端列表
     */
    List<RegisteredClientVO> listClients();

    /**
     * 根据客户端ID获取客户端详情
     * @param clientId 客户端ID
     * @return 客户端详情
     */
    RegisteredClientVO getClientByClientId(String clientId);

    /**
     * 重新生成客户端密钥
     * @param clientId 客户端ID
     * @return 新的客户端密钥
     */
    String regenerateClientSecret(String clientId);

    /**
     * 获取客户端授权历史
     * @param clientId 客户端ID
     * @return 授权历史列表
     */
    List<Object> getClientAuthorizationHistory(String clientId);
}
