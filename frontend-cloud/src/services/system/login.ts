import {request} from "@@/plugin-request/request";
import {SYSTEM} from "@/services/system/typings";


/** 登录接口 POST /api/login */
export async function login(body: SYSTEM.LoginBody, options?: { [key: string]: any }) {

  const formData = new URLSearchParams();
  formData.append('client_id', 'xunlu-admin-web');  // 使用公共客户端ID
  formData.append('grant_type', 'password');
  formData.append('username', body.username||'');
  formData.append('password', body.password||'');
  formData.append('uuid', body.uuid||'');
  formData.append('code', body.code||'');
  formData.append('scope', 'openid profile email all');  // 添加scope以支持userinfo端点

  return request<SYSTEM.LoginResult>('/api/oauth2/token', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
      // 公共客户端不需要Authorization header
    },
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

/** 刷新token */
export async function refreshToken(refreshTokenValue: string, options?: { [key: string]: any }) {
  const formData = new URLSearchParams();
  formData.append('client_id', 'xunlu-admin-web');
  formData.append('grant_type', 'refresh_token');
  formData.append('refresh_token', refreshTokenValue);

  return request<SYSTEM.LoginResult>('/api/oauth2/token', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
    },
    body: formData,
    skipErrorHandler: true, // 跳过错误处理，避免重复提示
    ...(options || {}),
  });
}
