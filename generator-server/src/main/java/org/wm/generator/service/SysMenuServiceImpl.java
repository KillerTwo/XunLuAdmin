package org.wm.generator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wm.generator.domain.SysMenu;
import org.wm.generator.mapper.SysMenuMapper;

import java.util.List;

/**
 * 功能描述：<功能描述>
 * 菜单Service
 * @author dove 
 * @date 2024/01/14 00:23
 * @since 1.0
**/
@RequiredArgsConstructor
@Service
public class SysMenuServiceImpl implements ISysMenuService {

    private final SysMenuMapper sysMenuMapper;

    private ThreadLocal<List<SysMenu>> allMenu = new ThreadLocal<>();

    @Override
    public List<SysMenu> selectSysMenuList(SysMenu sysMenu) {
        return sysMenuMapper.selectSysMenuList(sysMenu);
    }

    @Override
    public List<SysMenu> selectAll() {
        return sysMenuMapper.selectSysMenuAll();
    }

    @Override
    public List<SysMenu> selectChildren(Long parentId) {
        return sysMenuMapper.selectSysMenuListByParent(parentId);
    }

    @Override
    public int insertMenu(SysMenu sysMenu) {
        return sysMenuMapper.insertSysMenu(sysMenu);
    }

    @Override
    public int updateMenu(SysMenu sysMenu) {
        return sysMenuMapper.updateSysMenu(sysMenu);
    }


    @Override
    public List<SysMenu> listMenu() {
        var menuList = this.selectAll();
        allMenu.set(menuList);
        fillChildren(menuList);
        allMenu.remove();
        return menuList.stream().filter(m -> m.getParentId().equals(0L)).toList();
    }


    private void fillChildren(List<SysMenu> list) {
        if (list.isEmpty()) {
            return;
        }
        for (SysMenu sysMenu : list) {
            var children = findChildren(sysMenu.getId());
            if (!children.isEmpty()) {
                fillChildren(children);
            }
            sysMenu.setChildren(children);
        }
    }

    private List<SysMenu> findChildren(Long id) {
        var all = allMenu.get();
        return all.stream().filter(m -> m.getParentId().equals(id)).toList();
    }


    public static void main(String[] args) {
        var m1 = new SysMenu();
        m1.setName("menu1");
        m1.setId(1L);
        m1.setParentId(0L);
        var m2 = new SysMenu();
        m2.setName("menu2");
        m2.setId(2L);
        m2.setParentId(1L);
        var m3= new SysMenu();
        m3.setName("menu3");
        m3.setId(3L);
        m3.setParentId(1L);
        var m4 = new SysMenu();
        m4.setName("menu4");
        m4.setId(4L);
        m4.setParentId(3L);
        var m5 = new SysMenu();
        m5.setName("menu5");
        m5.setId(5L);
        m5.setParentId(0L);

        var list = List.of(m1, m2, m3, m4, m5);

        var instance = new SysMenuServiceImpl(null);
        instance.allMenu.set(list);
        instance.fillChildren(list);
        instance.allMenu.remove();

        System.err.println(list);


        var menuList = list.stream().filter(m -> m.getParentId().equals(0L)).toList();

        System.err.println(menuList);
    }


}
