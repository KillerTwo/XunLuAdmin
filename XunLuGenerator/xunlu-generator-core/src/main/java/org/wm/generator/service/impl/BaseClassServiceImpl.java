package org.wm.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.wm.generator.common.page.PageResult;
import org.wm.generator.common.query.Query;
import org.wm.generator.common.service.impl.BaseServiceImpl;
import org.wm.generator.dao.BaseClassDao;
import org.wm.generator.entity.BaseClassEntity;
import org.wm.generator.service.BaseClassService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 基类管理
 *
 * @author eumenides
 * 
 */
@Service
public class BaseClassServiceImpl extends BaseServiceImpl<BaseClassDao, BaseClassEntity> implements BaseClassService {

    @Override
    public PageResult<BaseClassEntity> page(Query query) {
        IPage<BaseClassEntity> page = baseMapper.selectPage(
                getPage(query), getWrapper(query)
        );

        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<BaseClassEntity> getList() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean save(BaseClassEntity entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}