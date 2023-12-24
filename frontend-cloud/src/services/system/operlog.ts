import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";

/** 查询操作日志列表 */
export async function sysOperlogList(params: SYSTEM.PageParams & SYSTEM.Operlog,
                                  options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/monitor/operlog/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

/** 清空操作日志 */
export async function cleanOperlog() {
  return request<SYSTEM.ResponseResult>('/api/monitor/operlog/clean', {
    method: 'DELETE'
  });
}

/** 删除操作日志 */
export async function removeOperlog(ids: (number | undefined)[]) {
  return request<SYSTEM.ResponseResult>(`/api/monitor/operlog/${ids}`, {
    method: 'DELETE'
  });
}
