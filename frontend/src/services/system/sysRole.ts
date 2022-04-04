import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";

/** 获取角色列表 GET /api/system/role/optionselect */
export async function sysRoleSelectList(
  options?: { [key: string]: any },
) {
  return request<SYSTEM.ResponseResult>('/api/system/role/optionselect', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取角色列表 GET /api/system/role/list */
export async function sysRoleList(
  params: {
    current?: number | 0,
    pageSize?: number | 10
  } & SYSTEM.SysRole|{},
  options?: { [key: string]: any },
) {
  return request<SYSTEM.ResponseResult>('/api/system/role/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...(options || {}),
  });
}

/** 新增角色 POST /api/system/role */
export async function addRole(sysRole?: SYSTEM.SysRole) {
  return request<SYSTEM.SysRole>('/api/system/role', {
    method: 'POST',
    data: {...sysRole, status: 0}
  });
}

/** 更新角色 UPDATE /api/system/role */
export async function updateRole(sysRole: SYSTEM.SysRole) {
  return request<SYSTEM.SysUser>('/api/system/role', {
    method: 'PUT',
    data: sysRole
  });
}

/** 删除角色 */
export async function removeRole(roleIds: (number | undefined)[]) {
  return request(`/api/system/role/${roleIds}`, {
    method: 'DELETE'
  })
}
