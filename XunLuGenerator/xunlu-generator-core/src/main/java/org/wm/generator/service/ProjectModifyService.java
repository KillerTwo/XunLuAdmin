package org.wm.generator.service;

import org.wm.generator.common.page.PageResult;
import org.wm.generator.common.query.Query;
import org.wm.generator.common.service.BaseService;
import org.wm.generator.entity.ProjectModifyEntity;

import java.io.IOException;

/**
 * 项目名变更
 *
 * @author eumenides
 * 
 */
public interface ProjectModifyService extends BaseService<ProjectModifyEntity> {

    PageResult<ProjectModifyEntity> page(Query query);

    byte[] download(ProjectModifyEntity project) throws IOException;

}