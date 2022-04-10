import {SYSTEM} from "@/services/system/typings";
import {request} from "@@/plugin-request/request";

/** 查询字典类型列表 */
export async function sysDictTypeList(params: SYSTEM.PageParams & SYSTEM.SysDictType,
                                    options?: { [key: string]: any },) {
  return request<SYSTEM.ResponseResult>('/api/system/dict/type/list', {
    method: 'GET',
    params: {
      ...params
    },
    ...options
  });
}

// optionselect
export async function dictTypeOptionSelect() {
  return request<SYSTEM.ResponseResult>('/api/system/dict/type/optionselect', {
    method: 'GET',
  });
}

/** 新增字典类型 */
export async function addSysDictType(sysDictType: SYSTEM.SysDictType) {
  return request<SYSTEM.ResponseResult>('/api/dict/type', {
    method: 'POST',
    data: sysDictType
  });
}

/** 修改字典类型 */
export async function updateSysDictType(sysDictType: SYSTEM.SysDictType) {
  return request<SYSTEM.ResponseResult>('/api/dict/type', {
    method: 'PUT',
    data: sysDictType
  });
}

/** 删除字典类型 */
export async function removeSysDictType(dictTypeIds: (number | undefined)[]) {
  return request<SYSTEM.ResponseResult>(`/api/dict/type/${dictTypeIds}`, {
    method: 'DELETE'
  });
}
