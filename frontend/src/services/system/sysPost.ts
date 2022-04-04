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

/** 岗位列表 GET /api/system/post */
export async function sysPostList(params: SYSTEM.PageParams & SYSTEM.SysPost) {
  return request<SYSTEM.SysUser>('/api/system/post/list', {
    method: 'GET',
    params: params
  });
}

/** 添加岗位 POST /api/system/post */
export async function addSysPost(sysPost: SYSTEM.SysPost) {
  return request<SYSTEM.SysUser>('/api/system/post', {
    method: 'POST',
    data: sysPost
  });
}

/** 更新岗位 PUT /api/system/post */
export async function updateSysPost(sysPost: SYSTEM.SysPost) {
  return request<SYSTEM.SysUser>('/api/system/post', {
    method: 'PUT',
    data: sysPost
  });
}

/** 删除岗位 DELETE /api/system/post */
export async function deleteSysPost(postIds: (number | undefined)[]) {
  return request<SYSTEM.SysUser>(`/api/system/post/${postIds}`, {
    method: 'DELETE'
  });
}
