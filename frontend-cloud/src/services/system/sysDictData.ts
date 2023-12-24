import {SYSTEM} from "@/services/system/typings";
import {request} from "@@/plugin-request/request";

/** 查询字典数据列表 */
export async function sysDictDataList(params: SYSTEM.PageParams & SYSTEM.SysDictData,
                                    options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/system/dict/data/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

/** 根据字典类型查询字典数据信息 */
export async function sysDictDataListByType(dictType: string) {
  return request<SYSTEM.ResponseResult>(`/api/system/dict/data/type/${dictType}`, {
    method: 'GET',
  });
}

/** 新增字典数据 */
export async function addSysDictData(sysDictData: SYSTEM.SysDictData) {
  return request<SYSTEM.ResponseResult>('/api/dict/data', {
    method: 'POST',
    data: sysDictData
  });
}

/** 修改字典数据 */
export async function updateSysDictData(sysDictData: SYSTEM.SysDictData) {
  return request<SYSTEM.ResponseResult>('/api/dict/data', {
    method: 'PUT',
    data: sysDictData
  });
}

/** 删除字典数据 */
export async function removeSysDictData(dictDataIds: (number | undefined)[]) {
  return request<SYSTEM.ResponseResult>(`/api/dict/data/${dictDataIds}`, {
    method: 'DELETE'
  });
}
