import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";


/** 登录接口 POST /api/login */
export async function login(body: SYSTEM.LoginBody, options?: { [key: string]: any }) {


  const formData = new URLSearchParams();
  formData.append('client_id', 'messaging-client-opaque');
  formData.append('grant_type', 'password');
  formData.append('username', body.username||'');
  formData.append('password', body.password||'');


  return request<SYSTEM.LoginResult>('/api/oauth2/token', {
    method: 'POST',
    headers: {
      // 'Content-Type': 'application/json',
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
      'Authorization': 'Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ='
    },
    // data: {'client_id': 'messaging-client-opaque', 'grant_type': 'password', 'username': 'admin', 'password': 'admin123'},
    body: formData,
    ...(options || {}),
  });
}

/** 当前用户 */
export async function getUserInfo(options?: { [key: string]: any }) {
  return request<any>("/api/system/getInfo", {
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
  return request<SYSTEM.ResponseResult>(`/api/validator/captchaImage`, {
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
