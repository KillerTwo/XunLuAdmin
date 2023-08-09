package org.wm.mapper;

import java.util.List;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.wm.data.annotation.UsePage;
import org.wm.domain.Oauth2RegisteredClient;

/**
 * 授权服务器客户端注册Mapper接口
 * 
 * @author wm
 * @date 2022-06-05
 */
@Mapper
public interface Oauth2RegisteredClientMapper {
    /**
     * 查询授权服务器客户端注册
     * 
     * @param id 授权服务器客户端注册主键
     * @return 授权服务器客户端注册
     */
    Oauth2RegisteredClient selectOauth2RegisteredClientById(String id);

    /**
     * 查询授权服务器客户端注册列表
     * 
     * @param oauth2RegisteredClient 授权服务器客户端注册
     * @return 授权服务器客户端注册集合
     */
    @UsePage
    PageInfo<Oauth2RegisteredClient> selectOauth2RegisteredClientList(Oauth2RegisteredClient oauth2RegisteredClient);

    /**
     * 新增授权服务器客户端注册
     * 
     * @param oauth2RegisteredClient 授权服务器客户端注册
     * @return 结果
     */
    int insertOauth2RegisteredClient(Oauth2RegisteredClient oauth2RegisteredClient);

    /**
     * 修改授权服务器客户端注册
     * 
     * @param oauth2RegisteredClient 授权服务器客户端注册
     * @return 结果
     */
    int updateOauth2RegisteredClient(Oauth2RegisteredClient oauth2RegisteredClient);

    /**
     * 删除授权服务器客户端注册
     * 
     * @param id 授权服务器客户端注册主键
     * @return 结果
     */
    int deleteOauth2RegisteredClientById(String id);

    /**
     * 批量删除授权服务器客户端注册
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteOauth2RegisteredClientByIds(String[] ids);
}
