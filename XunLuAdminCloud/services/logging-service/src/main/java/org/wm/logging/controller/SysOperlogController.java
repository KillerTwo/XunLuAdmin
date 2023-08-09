package org.wm.logging.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wm.commons.response.PageResult;
import org.wm.commons.response.ResponseResult;
import org.wm.commons.web.controller.BaseController;
import org.wm.logging.domain.SysOperLog;
import org.wm.logging.service.ISysOperLogService;


import java.util.List;

/**
 * 操作日志记录
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {
    @Autowired
    private ISysOperLogService operLogService;

    @GetMapping("/list")
    public PageResult<SysOperLog> list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @DeleteMapping("/{operIds}")
    public ResponseResult remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @DeleteMapping("/clean")
    public ResponseResult<?> clean() {
        operLogService.cleanOperLog();
        return ResponseResult.success();
    }
}
