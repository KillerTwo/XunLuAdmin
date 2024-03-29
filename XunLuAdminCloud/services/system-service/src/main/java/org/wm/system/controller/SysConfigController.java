package org.wm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.wm.commons.constants.UserConstants;
import org.wm.commons.response.PageResult;
import org.wm.commons.response.ResponseResult;
import org.wm.commons.web.controller.BaseController;
import org.wm.security.utils.SecurityUtils;
import org.wm.system.entity.SysConfig;
import org.wm.system.service.ISysConfigService;


import java.util.List;


/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    // @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public PageResult<SysConfig> list(SysConfig config) {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    /**
     * 根据参数编号获取详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public ResponseResult<SysConfig> getInfo(@PathVariable Long configId) {
        return ResponseResult.success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public ResponseResult<SysConfig> getConfigKey(@PathVariable String configKey) {
        return ResponseResult.success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    // @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping
    public ResponseResult<SysConfig> add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResponseResult.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUsername());
        return toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    // @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @PutMapping
    public ResponseResult<SysConfig> edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResponseResult.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    // @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/{configIds}")
    public ResponseResult<SysConfig> remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 刷新参数缓存
     */
    // @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/refreshCache")
    public ResponseResult<SysConfig> refreshCache() {
        configService.resetConfigCache();
        return ResponseResult.success();
    }
}
