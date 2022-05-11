import { request } from '@@/plugin-request/request';

// /tool/gen/list
export function listTable(
  params: Tool.PageParams & Tool.GenTable,
  options?: { [key: string]: any },
) {
  const a: Tool.TestA = {
    username: '12312',
  };
  console.log('testA: ', a);
  return request<Tool.ResponseResult>('/api/tool/gen/list', {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

// 表详情
export function getGenTable(tableId: number | undefined) {
  return request<Tool.ResponseResult>(`/api/tool/gen/${tableId}`, {
    method: 'GET',
  });
}

// 查询数据库所有表
export function listDbTable(params?: { [key: string]: any }) {
  return request<Tool.ResponseResult>('/api/tool/gen/db/list', {
    method: 'GET',
    params: params,
  });
}

// 导入
export function importTable(tableNames: string) {
  return request<Tool.ResponseResult>(`/api/tool/gen/importTable`, {
    method: 'POST',
    data: tableNames,
  });
}

// 生成代码
export function genCode(tableName: string | undefined) {
  return request<Tool.ResponseResult>(`/api/tool/gen/genCode/${tableName}`, {
    method: 'GET',
  });
}

// 同步数据库
export function synchDb(tableName: string | undefined) {
  return request<Tool.ResponseResult>(`/api/tool/gen/synchDb/${tableName}`, {
    method: 'GET',
  });
}

// 修改代码生成信息
export function updateGenTable(genTable: Tool.GenTable) {
  return request<Tool.ResponseResult>('/api/tool/gen', {
    method: 'PUT',
    data: genTable,
  });
}

// 生成预览
export function previewTable(tableId: number | undefined) {
  return request<Tool.ResponseResult>(`/api/tool/gen/preview/${tableId}`, {
    method: 'GET',
  });
}

// 删除
export function removeTable(tableIds: (number | undefined)[]) {
  return request<Tool.ResponseResult>(`/api/tool/gen/${tableIds}`, {
    method: 'DELETE',
  });
}
