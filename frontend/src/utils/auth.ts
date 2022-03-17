const TOKEN_KEY: string = 'ADMIN_TOKEN';

export function getToken(): string {
  return window.localStorage.getItem(TOKEN_KEY) || '';
}

export function setToken(token: string) {
  window.localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken() {
  window.localStorage.removeItem(TOKEN_KEY);
}
