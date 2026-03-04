import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";

/**
 * OAuth2客户端管理API
 * 参考GitHub OAuth Apps功能
 */

/** 获取OAuth2客户端列表 GET /api/oauth2/registeredClient/list */
export async function oauth2ClientList(
  params: {
    pageNum?: number;
    pageSize?: number;
    clientId?: string;
    clientName?: string;
  },
  options?: { [key: string]: any },
) {
  return request<SYSTEM.OAuth2ClientResponse>('/api/oauth2/registeredClient/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取OAuth2客户端详细信息 GET /api/oauth2/registeredClient/:id */
export async function getOAuth2Client(id: string) {
  return request<SYSTEM.ResponseResult>(`/api/oauth2/registeredClient/${id}`, {
    method: 'GET',
  });
}

/** 获取枚举值（授权类型、认证方法、作用域） GET /api/oauth2/registeredClient/enums */
export async function getOAuth2Enums() {
  return request<SYSTEM.ResponseResult>('/api/oauth2/registeredClient/enums', {
    method: 'GET',
  });
}

/** 新增OAuth2客户端 POST /api/oauth2/registeredClient */
export async function addOAuth2Client(data: SYSTEM.OAuth2Client) {
  return request<SYSTEM.ResponseResult>('/api/oauth2/registeredClient', {
    method: 'POST',
    data: data,
  });
}

/** 修改OAuth2客户端 PUT /api/oauth2/registeredClient */
export async function updateOAuth2Client(data: SYSTEM.OAuth2Client) {
  return request<SYSTEM.ResponseResult>('/api/oauth2/registeredClient', {
    method: 'PUT',
    data: data,
  });
}

/** 删除OAuth2客户端 DELETE /api/oauth2/registeredClient/:ids */
export async function removeOAuth2Client(ids: string[]) {
  return request<Record<string, any>>(`/api/oauth2/registeredClient/${ids.join(',')}`, {
    method: 'DELETE',
  });
}
