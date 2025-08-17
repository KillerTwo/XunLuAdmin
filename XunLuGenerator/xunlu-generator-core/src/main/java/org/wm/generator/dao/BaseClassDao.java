package org.wm.generator.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.wm.generator.entity.BaseClassEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基类管理
 *
 * @author eumenides
 * 
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface BaseClassDao extends BaseMapper<BaseClassEntity> {

}