import {SYSTEM} from "@/services/system/typings";
import {request} from "@@/plugin-request/request";

/** 查询定时任务列表 */
export async function sysJobList(params: SYSTEM.PageParams & SYSTEM.SysJob,
                                      options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/monitor/job/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

/** 新增定时任务 */
export async function addSysJob(sysJob: SYSTEM.SysJob) {
  return request<SYSTEM.ResponseResult>('/api/monitor/job', {
    method: 'POST',
    data: sysJob
  });
}

/** 修改定时任务 */
export async function updateSysJob(sysJob: SYSTEM.SysJob) {
  return request<SYSTEM.ResponseResult>('/api/monitor/job', {
    method: 'PUT',
    data: sysJob
  });
}

/** 删除定时任务 */
export async function removeSysJob(jobIds: (number | undefined)[]) {
  return request<SYSTEM.ResponseResult>(`/api/monitor/job/${jobIds}`, {
    method: 'DELETE'
  });
}

/** 执行定时任务 */
export async function runJob(jobId: number, jobGroup: string) {
  return request<SYSTEM.ResponseResult>(`/api/monitor/job/run`, {
    method: 'PUT',
    data: {jobId, jobGroup}
  });
}
