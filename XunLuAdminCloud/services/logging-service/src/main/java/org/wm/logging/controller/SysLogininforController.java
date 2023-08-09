package org.wm.logging.controller;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.wm.commons.response.PageResult;
import org.wm.commons.response.ResponseResult;
import org.wm.commons.web.controller.BaseController;
import org.wm.logging.domain.SysLogininfor;
import org.wm.logging.service.ISysLogininforService;


import java.util.List;

/**
 * 系统访问记录
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController {
    @Autowired
    private ISysLogininforService logininforService;

    @GetMapping("/list")
    public PageResult<SysLogininfor> list(SysLogininfor logininfor) {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }


    @DeleteMapping("/{infoIds}")
    public ResponseResult<?> remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }


    @DeleteMapping("/clean")
    public ResponseResult<?> clean() {
        logininforService.cleanLogininfor();
        return ResponseResult.success();
    }
}
