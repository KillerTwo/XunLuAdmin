package org.wm.generator.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.wm.generator.common.dao.BaseDao;
import org.wm.generator.entity.ProjectModifyEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名变更
 *
 * @author eumenides
 * 
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ProjectModifyDao extends BaseDao<ProjectModifyEntity> {

}