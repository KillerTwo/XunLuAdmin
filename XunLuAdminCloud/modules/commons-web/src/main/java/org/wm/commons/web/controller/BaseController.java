package org.wm.commons.web.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.response.PageResult;
import org.wm.commons.response.ResponseResult;
import org.wm.commons.utils.DateUtils;

import org.wm.commons.utils.StringUtils;


import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 */
@Slf4j
public class BaseController {

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(int pageNum, int pageSize) {
        // PageHelper.startPage(pageNum, pageSize);    分页调整为注解实现
    }


    @Deprecated
    protected void startPage() {

        // 分页调整为注解实现
        /*int pageNum = ServletUtils.getParameterToInt("current", 1);
        int pageSize = ServletUtils.getParameterToInt("pageSize", 10);
        PageHelper.startPage(pageNum, pageSize);*/
    }

    /**
     * 响应请求分页数据
     */
    @Deprecated
    protected <T> PageResult<T> getDataTable(List<T> list) {
        // 分页调整为注解实现
        // return PageResult.success(list, new PageInfo(list).getTotal());
        return PageResult.success(list, 100L);
    }


    /**
     * 返回成功
     */
    public ResponseResult success() {
        return ResponseResult.success();
    }

    /**
     * 返回失败消息
     */
    public ResponseResult error() {
        return ResponseResult.error();
    }

    /**
     * 返回成功消息
     */
    public ResponseResult success(String message) {
        return ResponseResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public ResponseResult error(String message) {
        return ResponseResult.error(message);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected ResponseResult toAjax(int rows) {
        return rows > 0 ? ResponseResult.success() : ResponseResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected ResponseResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser() {
        // TODO 获取当前登录用户  删除该方法，用其他的实现
        return null;
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录部门id
     */
    public Long getDeptId() {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername() {
        return getLoginUser().getUsername();
    }
}
