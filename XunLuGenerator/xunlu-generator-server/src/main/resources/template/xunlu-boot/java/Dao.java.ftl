package ${package}.${moduleName}.dao;

import ${package}.framework.mybatis.dao.BaseDao;
import ${package}.${moduleName}.entity.${ClassName}Entity;
import org.apache.ibatis.annotations.Mapper;

/**
* 功能描述：${tableComment}
*
* @author ${author} ${email}
* @date ${date}
* @since ${version}
**/
@Mapper
public interface ${ClassName}Dao extends BaseDao<${ClassName}Entity> {
	
}