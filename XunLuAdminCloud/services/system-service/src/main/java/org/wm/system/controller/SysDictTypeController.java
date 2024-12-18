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
import org.wm.system.entity.SysDictType;
import org.wm.system.service.ISysDictTypeService;


import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    // @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public PageResult<SysDictType> list(SysDictType dictType) {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }

    /**
     * 查询字典类型详细
     */
    // @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public ResponseResult<SysDictType> getInfo(@PathVariable Long dictId) {
        return ResponseResult.success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    // @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping
    public ResponseResult<?> add(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return ResponseResult.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    // @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PutMapping
    public ResponseResult<?> edit(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return ResponseResult.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    // @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @DeleteMapping("/{dictIds}")
    public ResponseResult<?> remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    /**
     * 刷新字典缓存
     */
    // @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @DeleteMapping("/refreshCache")
    public ResponseResult<?> refreshCache() {
        dictTypeService.resetDictCache();
        return ResponseResult.success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public ResponseResult<List<SysDictType>> optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return ResponseResult.success(dictTypes);
    }
}
