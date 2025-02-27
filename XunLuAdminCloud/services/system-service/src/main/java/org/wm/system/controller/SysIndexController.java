package org.wm.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wm.commons.utils.StringUtils;


/**
 * 首页
 *
 * @author ruoyi
 */
@RestController
public class SysIndexController {

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", "", "");
    }
}
