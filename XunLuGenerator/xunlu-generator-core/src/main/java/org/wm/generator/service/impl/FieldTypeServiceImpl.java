package org.wm.generator.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.wm.generator.common.page.PageResult;
import org.wm.generator.common.query.Query;
import org.wm.generator.common.service.impl.BaseServiceImpl;
import org.wm.generator.dao.FieldTypeDao;
import org.wm.generator.entity.FieldTypeEntity;
import org.wm.generator.service.FieldTypeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 字段类型管理
 *
 * @author eumenides
 * 
 */
@Service
public class FieldTypeServiceImpl extends BaseServiceImpl<FieldTypeDao, FieldTypeEntity> implements FieldTypeService {

    @Override
    public PageResult<FieldTypeEntity> page(Query query) {
        IPage<FieldTypeEntity> page = baseMapper.selectPage(
                getPage(query),
                getWrapper(query)
        );
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public Map<String, FieldTypeEntity> getMap() {
        List<FieldTypeEntity> list = baseMapper.selectList(null);
        Map<String, FieldTypeEntity> map = new LinkedHashMap<>(list.size());
        for (FieldTypeEntity entity : list) {
            map.put(entity.getColumnType().toLowerCase(), entity);
        }
        return map;
    }

    @Override
    public Set<String> getPackageByTableId(Long tableId) {
        Set<String> importList = baseMapper.getPackageByTableId(tableId);

        return importList.stream().filter(StrUtil::isNotBlank).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getList() {
        return baseMapper.list();
    }

    @Override
    public boolean save(FieldTypeEntity entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}