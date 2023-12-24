import {SYSTEM} from "@/services/system/typings";
import {request} from "@@/plugin-request/request";

/** 查询配置列表 */
export async function sysConfigList(params: SYSTEM.PageParams & SYSTEM.SysConfig,
                                  options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/system/config/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

/** 新增配置 */
export async function addSysConfig(sysConfig: SYSTEM.SysConfig) {
  return request<SYSTEM.ResponseResult>('/api/system/config', {
    method: 'POST',
    data: sysConfig
  });
}

/** 修改配置 */
export async function updateSysConfig(sysConfig: SYSTEM.SysConfig) {
  return request<SYSTEM.ResponseResult>('/api/system/config', {
    method: 'PUT',
    data: sysConfig
  });
}

/** 删除配置 */
export async function removeSysConfig(configIds: (number | undefined)[]) {
  return request<SYSTEM.ResponseResult>(`/api/system/config/${configIds}`, {
    method: 'DELETE'
  });
}
