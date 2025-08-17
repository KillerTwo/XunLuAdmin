package org.wm.generator.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.wm.generator.entity.DataSourceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源管理
 *
 * @author eumenides
 * 
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface DataSourceDao extends BaseMapper<DataSourceEntity> {

}