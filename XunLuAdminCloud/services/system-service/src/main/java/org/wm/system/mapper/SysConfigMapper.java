package org.wm.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.wm.commons.dto.MyPageParam;
import org.wm.data.annotation.UsePage;
import org.wm.system.entity.SysConfig;


import java.util.List;

 /**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 参数配置 数据层
 */
 @Mapper
 public interface SysConfigMapper {
    /**
     * 查询参数配置信息
     *
     * @param config 参数配置信息
     * @return 参数配置信息
     */
    SysConfig selectConfig(SysConfig config);

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */

    List<SysConfig> selectConfigList(SysConfig config);


    /**
     * 功能描述：<功能描述>
     *       测试方法，生产环境不能使用
     * @author dove
     * @date 2023/7/19 23:00
     * @param param MyPageParam
     * @return java.util.List<org.wm.system.entity.SysConfig>
     * @throws
     */
    @Deprecated
    @UsePage
    List<SysConfig> selectConfigListPage(MyPageParam param);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数配置信息
     */
    SysConfig checkConfigKeyUnique(String configKey);

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    int insertConfig(SysConfig config);

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    int updateConfig(SysConfig config);

    /**
     * 删除参数配置
     *
     * @param configId 参数ID
     * @return 结果
     */
    int deleteConfigById(Long configId);

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    int deleteConfigByIds(Long[] configIds);
}
