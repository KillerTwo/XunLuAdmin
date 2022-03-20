import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";

/** 获取岗位列表 GET /api/system/post/optionselect */
export async function sysPostSelectList(
  options?: { [key: string]: any },
) {
  return request<SYSTEM.ResponseResult>('/api/system/post/optionselect', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 用户 POST /api/system/user */
export async function addUser(options?: { [key: string]: any }) {
  return request<SYSTEM.SysUser>('/api/system/user', {
    method: 'POST',
    ...(options || {}),
  });
}
