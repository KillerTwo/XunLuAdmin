import {SYSTEM} from "@/services/system/typings";
import {request} from "@@/plugin-request/request";

/** 查询定时任务日志列表 */
export async function sysJobLogList(params: SYSTEM.PageParams & SYSTEM.SysJobLog,
                                      options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/monitor/jobLog/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

/** 删除定时任务日志 */
export async function removeSysJobLog(jobLogIds: (number | undefined)[]) {
  return request<SYSTEM.ResponseResult>(`/api/monitor/jobLog/${jobLogIds}`, {
    method: 'DELETE'
  });
}
