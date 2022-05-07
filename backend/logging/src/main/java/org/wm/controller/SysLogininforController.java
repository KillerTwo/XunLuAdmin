package org.wm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wm.domain.SysLogininfor;
import org.wm.response.PageResult;
import org.wm.response.ResponseResult;
import org.wm.service.ISysLogininforService;

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

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    public PageResult<SysLogininfor> list(SysLogininfor logininfor) {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @DeleteMapping("/{infoIds}")
    public ResponseResult remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @DeleteMapping("/clean")
    public ResponseResult<?> clean() {
        logininforService.cleanLogininfor();
        return ResponseResult.success();
    }
}
