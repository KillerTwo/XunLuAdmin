import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";

/** 获取用户列表 GET /api/system/user/list */
export async function sysUserList(
  params: {
    /** 当前的页码 */
    pageNum?: number;
    /** 页面的容量 */
    pageSize?: number;
    deptId?: number|null;
    userName?: string|null;
    nickName?: string|null;
  },
  options?: { [key: string]: any },
) {
  return request<SYSTEM.SysUser>('/api/system/user/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 用户 POST /api/system/user */
export async function addUser(sysUser: SYSTEM.SysUser) {
  return request<SYSTEM.SysUser>('/api/system/user', {
    method: 'POST',
    data: sysUser
  });
}

/** 用户 UPDATE /api/system/user */
export async function updateUser(sysUser: SYSTEM.SysUser) {
  return request<SYSTEM.ResponseResult>('/api/system/user', {
    method: 'PUT',
    data: sysUser
  });
}

/** 删除用户 DELETE /api/system/user */
export async function removeUser(userIds: (number | undefined)[]) {
  return request<Record<string, any>>(`/api/system/user/${userIds}`, {
    method: 'DELETE',
  });
}

/** 获取用户详情 GET /api/system/user/{userId} */
export async function getSysUserInfo(
  userId: number | undefined
) {
  return request<any>(`/api/system/user/${userId}`, {
    method: 'GET',
  });
}
