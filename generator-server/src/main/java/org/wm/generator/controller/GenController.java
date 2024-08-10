package org.wm.generator.controller;


import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.wm.generator.domain.GenTable;
import org.wm.generator.domain.GenTableColumn;
import org.wm.generator.response.PageResult;
import org.wm.generator.response.ResponseResult;
import org.wm.generator.service.IGenTableColumnService;
import org.wm.generator.service.IGenTableService;



import org.wm.generator.util.Convert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成 操作处理
 */
@RestController
@RequestMapping("/tool/gen")
public class GenController {

    @Autowired
    private IGenTableService genTableService;

    @Autowired
    private IGenTableColumnService genTableColumnService;

    /**
     * 查询代码生成列表
     */
    @GetMapping("/list")
    public PageResult<GenTable> genList(GenTable genTable) {
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        return PageResult.success(list, ((Page<?>)list).getTotal());
    }

    /**
     * 修改代码生成业务
     */
    @GetMapping(value = "/{tableId}")
    public ResponseResult<?> getInfo(@PathVariable Long tableId) {
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", table);
        map.put("rows", list);
        map.put("tables", tables);
        return ResponseResult.success(map);
    }

    /**
     * 查询数据库列表
     */
    @GetMapping("/db/list")
    public PageResult<GenTable> dataList(GenTable genTable) {
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return PageResult.success(list, ((Page<?>)list).getTotal());
    }

    /**
     * 查询数据表字段列表
     */
    @GetMapping(value = "/column/{tableId}")
    public PageResult<GenTableColumn> columnList(Long tableId) {
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        return PageResult.success(list, Integer.toUnsignedLong(list.size()));
    }

    /**
     * 导入表结构（保存）
     */
    @PostMapping("/importTable")
    public ResponseResult<?> importTableSave(@RequestBody String tables) {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
        return ResponseResult.success();
    }

    /**
     * 修改保存代码生成业务
     */
    @PutMapping
    public ResponseResult<?> editSave(@Validated @RequestBody GenTable genTable) {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return ResponseResult.success();
    }

    /**
     * 删除代码生成
     */
    @DeleteMapping("/{tableIds}")
    public ResponseResult<?> remove(@PathVariable Long[] tableIds) {
        genTableService.deleteGenTableByIds(tableIds);
        return ResponseResult.success();
    }

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    public ResponseResult<?> preview(@PathVariable("tableId") Long tableId) throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return ResponseResult.success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     */
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
    @GetMapping("/genCode/{tableName}")
    public ResponseResult<?> genCode(@PathVariable("tableName") String tableName) {
        genTableService.generatorCode(tableName);
        return ResponseResult.success();
    }

    /**
     * 同步数据库
     */
    @GetMapping("/synchDb/{tableName}")
    public ResponseResult<?> synchDb(@PathVariable("tableName") String tableName) {
        genTableService.synchDb(tableName);
        return ResponseResult.success();
    }

    /**
     * 批量生成代码
     */
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"we-master.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}