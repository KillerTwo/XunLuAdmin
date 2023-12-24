import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";

/** 查询登录日志列表 */
export async function sysLogininforList(params: SYSTEM.PageParams & SYSTEM.SysLogininfor,
                                  options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/monitor/logininfor/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

/** 清空登录日志 */
export async function cleanLogininfor() {
  return request<SYSTEM.ResponseResult>('/api/monitor/logininfor/clean', {
    method: 'DELETE'
  });
}

/** 删除登录日志 */
export async function removeLogininfor(ids: (number | undefined)[]) {
  return request<SYSTEM.ResponseResult>(`/api/monitor/logininfor/${ids}`, {
    method: 'DELETE'
  });
}
