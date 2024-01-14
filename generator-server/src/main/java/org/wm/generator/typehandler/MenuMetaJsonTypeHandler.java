package org.wm.generator.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.wm.generator.domain.SysMenu;
import org.wm.generator.util.ObjectMapperUtil;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2024/01/14 14:50
 * @since 1.0
**/
public class MenuMetaJsonTypeHandler extends BaseTypeHandler<SysMenu.Meta> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SysMenu.Meta parameter, JdbcType jdbcType) throws SQLException {
        var value = ObjectMapperUtil.writeValueAsString(parameter);
        ps.setString(i, value);
    }

    @Override
    public SysMenu.Meta getNullableResult(ResultSet rs, String columnName) throws SQLException {
        var value = rs.getString(columnName);
        return ObjectMapperUtil.readValue(value, SysMenu.Meta.class);
    }

    @Override
    public SysMenu.Meta getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        var value = rs.getString(columnIndex);
        return ObjectMapperUtil.readValue(value, SysMenu.Meta.class);
    }

    @Override
    public SysMenu.Meta getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        var value = cs.getString(columnIndex);
        return ObjectMapperUtil.readValue(value, SysMenu.Meta.class);
    }
}
