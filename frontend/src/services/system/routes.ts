import { request } from 'umi';
import {SYSTEM} from "@/services/system/typings";

export async function fetchMenuData(options?: { [key: string]: any }) {
  return request<{data: SYSTEM.Router[]}>('/api/getRouters', {
    method: 'GET',
    ...(options || {}),
  });
}
