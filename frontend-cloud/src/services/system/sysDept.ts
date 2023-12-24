import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";

/** 获取部门列表 GET /api/system/dept/optionselect */
export async function sysDeptSelectList(
  options?: { [key: string]: any },
) {
  return request<SYSTEM.ResponseResult>('/api/system/dept/treeselect', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 查询部门列表 */
export async function sysDeptList(params: SYSTEM.SysDept,
                                  options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/system/dept/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

/** 新增部门 */
export async function addSysDept(sysMenu: SYSTEM.SysDept) {
  return request<SYSTEM.SysDept>('/api/system/dept', {
    method: 'POST',
    data: {...sysMenu, status: 0}
  });
}

/** 修改部门 */
export async function updateSysDept(sysMenu: SYSTEM.SysDept) {
  return request<SYSTEM.SysDept>('/api/system/dept', {
    method: 'PUT',
    data: {...sysMenu}
  });
}

/** 删除部门 */
export async function removeSysDept(menuIds: (number | undefined)[]) {
  return request<SYSTEM.SysDept>(`/api/system/dept/${menuIds}`, {
    method: 'DELETE'
  });
}
