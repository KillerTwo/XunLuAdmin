const TOKEN_KEY: string = 'ADMIN_TOKEN';
const REFRESH_TOKEN_KEY: string = 'ADMIN_REFRESH_TOKEN';
const AUTO_LOGIN_KEY: string = 'ADMIN_AUTO_LOGIN';
const TOKEN_EXPIRE_TIME_KEY: string = 'ADMIN_TOKEN_EXPIRE_TIME';

export function getToken(): string {
  return window.localStorage.getItem(TOKEN_KEY) || '';
}

export function setToken(token: string) {
  window.localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken() {
  window.localStorage.removeItem(TOKEN_KEY);
}

export function getRefreshToken(): string {
  return window.localStorage.getItem(REFRESH_TOKEN_KEY) || '';
}

export function setRefreshToken(refreshToken: string) {
  window.localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
}

export function clearRefreshToken() {
  window.localStorage.removeItem(REFRESH_TOKEN_KEY);
}

export function getAutoLogin(): boolean {
  return window.localStorage.getItem(AUTO_LOGIN_KEY) === 'true';
}

export function setAutoLogin(autoLogin: boolean) {
  window.localStorage.setItem(AUTO_LOGIN_KEY, autoLogin ? 'true' : 'false');
}

export function clearAutoLogin() {
  window.localStorage.removeItem(AUTO_LOGIN_KEY);
}

export function getTokenExpireTime(): number {
  const expireTime = window.localStorage.getItem(TOKEN_EXPIRE_TIME_KEY);
  return expireTime ? parseInt(expireTime, 10) : 0;
}

export function setTokenExpireTime(expiresIn: number) {
  // 保存token过期时间戳（当前时间 + 过期秒数）
  const expireTime = Date.now() + expiresIn * 1000;
  window.localStorage.setItem(TOKEN_EXPIRE_TIME_KEY, expireTime.toString());
}

export function clearTokenExpireTime() {
  window.localStorage.removeItem(TOKEN_EXPIRE_TIME_KEY);
}

export function isTokenExpired(): boolean {
  const expireTime = getTokenExpireTime();
  if (!expireTime) return true;
  // 提前5分钟刷新token
  return Date.now() > expireTime - 5 * 60 * 1000;
}

export function clearAllAuth() {
  clearToken();
  clearRefreshToken();
  clearAutoLogin();
  clearTokenExpireTime();
}
