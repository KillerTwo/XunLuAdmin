import {SYSTEM} from "@/services/system/typings";
import {request} from "@@/plugin-request/request";


/** 查询树形菜单 */
export async function sysMenuSelectList() {
  return request<SYSTEM.ResponseResult>('/api/system/menu/treeselect', {
    method: "GET"
  });
}

// /roleMenuTreeselect/{roleId}
/** 查询指定角色的树形菜单 */
export async function sysMenuSelectRoleList(roleId: number | undefined) {
  return request<SYSTEM.ResponseResult>(`/api/system/menu/roleMenuTreeselect/${roleId}`, {
    method: "GET"
  });
}

/** 查询菜单列表 */
export async function sysMenuList(params: SYSTEM.SysMenu,
                                  options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/system/menu/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

/** 新增菜单 */
export async function addSysMenu(sysMenu: SYSTEM.SysMenu) {
  return request<SYSTEM.SysMenu>('/api/system/menu', {
    method: 'POST',
    data: {...sysMenu, status: 0}
  });
}

/** 修改菜单 */
export async function updateSysMenu(sysMenu: SYSTEM.SysMenu) {
  return request<SYSTEM.SysMenu>('/api/system/menu', {
    method: 'PUT',
    data: {...sysMenu}
  });
}

/** 删除菜单 */
export async function removeSysMenu(menuIds: (number | undefined)[]) {
  return request<SYSTEM.SysMenu>(`/api/system/menu/${menuIds}`, {
    method: 'DELETE'
  });
}
