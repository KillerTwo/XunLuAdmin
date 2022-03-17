import { request } from 'umi';

export async function fetchMenuData(options?: { [key: string]: any }) {
  return request<{data: SYSTEM.Router[]}>('/api/getRouters', {
    method: 'GET',
    ...(options || {}),
  });
}
