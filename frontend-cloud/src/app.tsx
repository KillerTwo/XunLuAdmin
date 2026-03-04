import type { Settings as LayoutSettings } from '@ant-design/pro-layout';
import { SettingDrawer } from '@ant-design/pro-layout';
import { PageLoading } from '@ant-design/pro-layout';
import type { RunTimeLayoutConfig } from 'umi';

import { history, Link } from 'umi';
import RightContent from '@/components/RightContent';
import Footer from '@/components/Footer';
// import { currentUser as queryCurrentUser } from './services/ant-design-pro/api';
import { BookOutlined, LinkOutlined } from '@ant-design/icons';
import defaultSettings from '../config/defaultSettings';
// import { fetchMenuData } from './services/system/routes';
import type { RequestConfig } from '@@/plugin-request/request';
import type { RequestOptionsInit } from 'umi-request';
import errorHandler from '@/utils/errorHandler';
import { fetchMenuData } from '@/services/system/routes';
// import LoadingComponent from "@ant-design/pro-layout/es/PageLoading";
import { getUserInfo, outLogin } from '@/services/system/login';
// import {fetchMenuData} from "@/services/system/routes";

import { SYSTEM } from '@/services/system/typings';
// import iconMapping from "@/utils/iconMap";
// import React from "react";
import { handleIconAndComponent } from '@/utils/routes';
// import LoadingComponent from "@ant-design/pro-layout/es/PageLoading";
import {
  clearToken,
  getToken,
  getRefreshToken,
  getAutoLogin,
  setToken,
  setRefreshToken,
  setTokenExpireTime,
  isTokenExpired,
  clearAllAuth
} from '@/utils/auth';
import { notification } from 'antd';
import { stringify } from 'querystring';
import { refreshToken as refreshTokenAPI } from '@/services/system/login';

// const isDev = process.env.NODE_ENV === 'development';
const isDev = false;
const loginPath = '/user/login';

/** 防止重复显示401提示 */
let hasShown401Notification = false;
let notification401Timer: NodeJS.Timeout | null = null;

/** 防止重复刷新token */
let isRefreshing = false;
let refreshSubscribers: ((token: string) => void)[] = [];

/** 订阅token刷新 */
function subscribeTokenRefresh(callback: (token: string) => void) {
  refreshSubscribers.push(callback);
}

/** 通知订阅者token已刷新 */
function onTokenRefreshed(token: string) {
  refreshSubscribers.forEach(callback => callback(token));
  refreshSubscribers = [];
}

/** 刷新access_token */
async function tryRefreshToken(): Promise<boolean> {
  const refreshTokenValue = getRefreshToken();
  const autoLogin = getAutoLogin();

  if (!refreshTokenValue || !autoLogin) {
    return false;
  }

  try {
    const result = await refreshTokenAPI(refreshTokenValue);

    if (result?.access_token) {
      // 更新token
      setToken(result.access_token);

      // 更新refresh_token（如果返回了新的）
      if (result.refresh_token) {
        setRefreshToken(result.refresh_token);
      }

      // 更新token过期时间
      if (result.expires_in) {
        setTokenExpireTime(result.expires_in);
      }

      return true;
    }

    return false;
  } catch (error) {
    console.error('刷新token失败:', error);
    return false;
  }
}

/** 获取用户信息比较慢的时候会展示一个 loading */
export const initialStateConfig = {
  loading: <PageLoading />,
};

const authHeaderInterceptor = async (url: string, options: RequestOptionsInit) => {
  if (url.indexOf('/login/') !== -1 || url.indexOf('/logout') !== -1 || url.indexOf("token") !== -1) {
    return {
      url: `${url}`,
      options: { ...options, interceptors: true },
    };
  } else {
    let token = getToken();

    // 检查token是否过期，如果过期且开启了自动登录，则尝试刷新token
    if (token && isTokenExpired() && getAutoLogin()) {
      if (!isRefreshing) {
        isRefreshing = true;
        const refreshSuccess = await tryRefreshToken();
        isRefreshing = false;

        if (refreshSuccess) {
          token = getToken();
          onTokenRefreshed(token);
        } else {
          // 刷新失败，清除所有认证信息
          clearAllAuth();
          onTokenRefreshed('');
        }
      } else {
        // 如果正在刷新token，等待刷新完成
        token = await new Promise((resolve) => {
          subscribeTokenRefresh((newToken: string) => {
            resolve(newToken);
          });
        });
      }
    }

    console.log('url, token: ', url, token);
    let authHeader = {};
    if (token) {
      authHeader = { Authorization: `Bearer ${token}` };
    }

    return {
      url: `${url}`,
      options: { ...options, interceptors: true, headers: authHeader },
    };
  }
};

const demoResponseInterceptors = async (response: Response, r: RequestOptionsInit) => {
  console.log('response: ', response);
  console.log('RequestOptionsInit', r);
  if (r.responseType === 'blob') {
    return response;
  }
  const data = await response.clone().json();
  console.log('demoResponseInterceptors data: ', data);
  if (
    data.code &&
    data.code !== 200 &&
    [300, 301, 302, 303, 304, 305, 306, 307, 308, 309].indexOf(data.code) === -1
  ) {
    if (data.code === 401) {
      // 尝试使用refresh_token刷新access_token
      const autoLogin = getAutoLogin();
      const refreshTokenValue = getRefreshToken();

      if (autoLogin && refreshTokenValue && !isRefreshing) {
        isRefreshing = true;
        const refreshSuccess = await tryRefreshToken();
        isRefreshing = false;

        if (refreshSuccess) {
          // 刷新成功，重新发起原请求
          // 注意：这里可能需要根据实际情况调整
          console.log('Token刷新成功，请重新操作');
          return response;
        }
      }

      // 刷新失败或未开启自动登录，执行原有的401处理逻辑
      // 防止重复提示：使用防抖机制，500ms内只显示一次
      if (!hasShown401Notification) {
        hasShown401Notification = true;

        // 根据当前页面状态显示不同的提示
        const isOnLoginPage = window.location.pathname === '/user/login' ||
                             window.location.pathname === '/user/resetPassword';

        if (!isOnLoginPage) {
          // 只在非登录页面显示提示
          notification.warning({
            message: '登录状态已失效',
            description: '您的登录已过期，请重新登录',
            duration: 3,
          });
        }

        // 500ms后重置标志，允许下次显示
        if (notification401Timer) {
          clearTimeout(notification401Timer);
        }
        notification401Timer = setTimeout(() => {
          hasShown401Notification = false;
        }, 500);
      }

      await outLogin();
      clearAllAuth(); // 清除所有认证信息
      const { query = {}, search, pathname } = history.location;
      const { redirect } = query;
      // Note: There may be security issues, please note
      if (
        window.location.pathname !== '/user/login' &&
        window.location.pathname !== '/user/resetPassword' &&
        !redirect
      ) {
        history.replace({
          pathname: '/user/login',
          search: stringify({
            redirect: pathname + search,
          }),
        });
      }
    }/* else {
      notification.error({
        message: `请求错误 ${data.code}`,
        description: data.msg,
      });
    }*/
  }
  return response;
};

export const request: RequestConfig = {
  errorHandler,
  // 新增自动添加AccessToken的请求前拦截器
  requestInterceptors: [authHeaderInterceptor],
  responseInterceptors: [demoResponseInterceptors],
};

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: any;
  loading?: boolean;
  fetchUserInfo?: () => Promise<any | undefined>;
  fetchMenuInfo?: () => Promise<SYSTEM.Router[] | undefined>;
  customMenuData?: SYSTEM.Router[];
}> {
  const fetchUserInfo = async () => {
    try {
      const msg = await getUserInfo();
      return msg.data;
    } catch (error) {
      // 不在这里跳转，由拦截器统一处理
      // history.push(loginPath);
      return undefined;
    }
  };

  const fetchMenuInfo = async () => {
    try {
      const menuData = await fetchMenuData();
      console.log('fetchMenuDatas.menuData: ', menuData);
      return menuData.data;
    } catch (error) {
      // 菜单获取失败也不处理，由拦截器统一处理
      return undefined;
    }
  };

  console.log('defaultSetting: ', defaultSettings);
  // 如果是登录页面，不执行
  if (
    history.location.pathname !== loginPath &&
    history.location.pathname !== '/user/resetPassword'
  ) {
    // 重置401提示标志，允许显示提示
    hasShown401Notification = false;

    // 检查是否有token，如果没有但有refresh_token和autoLogin，尝试自动登录
    const token = getToken();
    const refreshTokenValue = getRefreshToken();
    const autoLogin = getAutoLogin();

    if (!token && refreshTokenValue && autoLogin) {
      console.log('尝试使用refresh_token自动登录');
      const refreshSuccess = await tryRefreshToken();
      if (!refreshSuccess) {
        // 自动登录失败，清除所有认证信息并跳转到登录页
        clearAllAuth();
        history.push(loginPath);
        return {
          fetchUserInfo,
          fetchMenuInfo,
          settings: defaultSettings,
        };
      }
    }

    const currentUser = await fetchUserInfo();
    const customMenuData = await fetchMenuInfo();
    return {
      fetchUserInfo,
      fetchMenuInfo,
      currentUser,
      customMenuData,
      settings: defaultSettings,
    };
  }

  // const customMenuData = await fetchMenuInfo();
  return {
    fetchUserInfo,
    fetchMenuInfo,
    settings: defaultSettings,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    menu: {
      // 每当 initialState?.currentUser?.userid 发生修改时重新执行 request
      params: initialState,
      request: async (params, defaultMenuData) => {
        console.log("menu params", params);
        console.log("menu defaultMenuData: ", defaultMenuData);
        // initialState.currentUser 中包含了所有用户信息
        // const menuData = await fetchMenuData();
        // return menuData;
        const resMenuData = initialState?.customMenuData || [];
        console.log("resMenuData: ", resMenuData);
        const menus = handleIconAndComponent(resMenuData);
        return menus;
      },
    },
    rightContentRender: () => <RightContent />,
    disableContentMargin: false,
    waterMarkProps: {
      content: initialState?.currentUser?.name,
    },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      // 如果没有登录，重定向到 login
      if (
        !initialState?.currentUser &&
        location.pathname !== loginPath &&
        location.pathname !== '/user/resetPassword'
      ) {
        history.push(loginPath);
      }
    },
    links: isDev
      ? [
          <Link to="/umi/plugin/openapi" target="_blank">
            <LinkOutlined />
            <span>OpenAPI 文档</span>
          </Link>,
          <Link to="/~docs">
            <BookOutlined />
            <span>业务组件文档</span>
          </Link>,
        ]
      : [],
    menuHeaderRender: undefined,
    menuItemRender: (menuItemProps, defaultDom) => {
      if (menuItemProps.isUrl || !menuItemProps.path) {
        return defaultDom;
      }
      // 支持二级菜单显示icon
      return (
        <Link to={menuItemProps.path}>
          {menuItemProps.pro_layout_parentKeys &&
            menuItemProps.pro_layout_parentKeys.length > 0 &&
            menuItemProps.icon}
          {defaultDom}
        </Link>
      );
    },
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children, props) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          {!props.location?.pathname?.includes('/login') ? (
            <SettingDrawer
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          ) :
            (
              <SettingDrawer
                enableDarkTheme
                settings={initialState?.settings}
                themeOnly={true}
                onSettingChange={(settings) => {
                  setInitialState((preInitialState) => ({
                    ...preInitialState,
                    settings,
                  }));
                }}
              />
            )
          }
        </>
      );
    },
    ...initialState?.settings,
  };
};
