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

/** 新增角色 POST /api/system/role */
export async function addUser(options?: { [key: string]: any }) {
  return request<SYSTEM.SysRole>('/api/system/role', {
    method: 'POST',
    ...(options || {}),
  });
}
