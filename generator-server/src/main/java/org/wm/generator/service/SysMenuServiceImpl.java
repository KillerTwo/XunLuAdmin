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
}
