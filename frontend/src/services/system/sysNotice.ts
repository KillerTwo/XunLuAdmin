import {SYSTEM} from "@/services/system/typings";
import {request} from "@@/plugin-request/request";

/** 查询通知公告列表 */
export async function sysNoticeList(params: SYSTEM.PageParams & SYSTEM.SysNotice,
                                  options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/system/notice/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

/** 新增通知公告 */
export async function addSysNotice(sysNotice: SYSTEM.SysNotice) {
  return request<SYSTEM.ResponseResult>('/api/system/notice', {
    method: 'POST',
    data: sysNotice
  });
}

/** 修改通知公告 */
export async function updateSysNotice(sysNotice: SYSTEM.SysNotice) {
  return request<SYSTEM.ResponseResult>('/api/system/notice', {
    method: 'PUT',
    data: sysNotice
  });
}

/** 删除通知公告 */
export async function removeSysNotice(noticeId: (number | undefined)[]) {
  return request<SYSTEM.ResponseResult>(`/api/system/notice/${noticeId}`, {
    method: 'DELETE'
  });
}
