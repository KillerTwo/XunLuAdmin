import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";

/** 登录接口 POST /api/login */
export async function login(body: SYSTEM.LoginBody, options?: { [key: string]: any }) {
  return request<SYSTEM.LoginResult>('/api/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 当前用户 */
export async function getUserInfo(options?: { [key: string]: any }) {
  return request<any>("/api/getInfo", {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    ...(options || {}),
  })
}

/** 退出登录 */
export async function outLogin(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 发送短信验证码 POST /api/login/captcha "data:image/gif;base64," + res.img */
export async function getFakeCaptcha(data: SYSTEM.PhoneCodeBody) {
  return request<SYSTEM.ResponseResult>(`/api/captcha`, {
    method: 'POST',
    data: data
  });
}

/** 获取验证码 **/
export async function getCaptchaImage() {
  return request<SYSTEM.ResponseResult>(`/api/captchaImage`, {
    method: 'GET'
  });
}

/** 重置密码 */
export async function resetPassword(body: SYSTEM.ResetPasswordBody) {
  return request<SYSTEM.LoginResult>('/api/resetPassword', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
  });
}
