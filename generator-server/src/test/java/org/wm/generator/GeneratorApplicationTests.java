package org.wm.generator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wm.generator.domain.SysMenu;
import org.wm.generator.mapper.SysMenuMapper;

@SpringBootTest
class GeneratorApplicationTests {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Test
    void contextLoads() {

        var menu = new SysMenu();
        menu.setName("BackAuthBtn");
        menu.setPath("btn");
        menu.setComponent("/demo/permission/back/Btn");
        menu.setRedirect("");
        menu.setParentId(5L);

        var meta = new SysMenu.Meta();
        meta.setCurrentActiveMenu("");
        meta.setIcon("carbon:user-role");
        meta.setTitle("routes.demo.permission.backBtn");
        meta.setHideBreadcrumb(false);
        meta.setHideMenu(false);


        menu.setMeta(meta);
        sysMenuMapper.insertSysMenu(menu);

    }

}
