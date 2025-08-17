package org.wm.generator.service;

import org.wm.generator.common.page.PageResult;
import org.wm.generator.common.query.Query;
import org.wm.generator.common.service.BaseService;
import org.wm.generator.entity.BaseClassEntity;

import java.util.List;

/**
 * 基类管理
 *
 * @author eumenides
 * 
 */
public interface BaseClassService extends BaseService<BaseClassEntity> {

    PageResult<BaseClassEntity> page(Query query);

    List<BaseClassEntity> getList();
}