package org.wm.generator.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.wm.generator.entity.TableEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据表
 *
 * @author eumenides
 * 
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface TableDao extends BaseMapper<TableEntity> {

}
