import {
  AlipayCircleOutlined,
  LockOutlined,
  MobileOutlined,
  TaobaoCircleOutlined,
  UserOutlined,
  WeiboCircleOutlined,
} from '@ant-design/icons';
import { Alert, message, Tabs } from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {ProFormCaptcha, ProFormCheckbox, ProFormText, LoginForm, ProFormInstance} from '@ant-design/pro-form';
import { useIntl, history, FormattedMessage, useModel } from 'umi';
import Footer from '@/components/Footer';
import {getCaptchaImage, getFakeCaptcha, login} from '@/services/system/login'

import styles from './index.less';
import {SYSTEM} from "@/services/system/typings";

import { setToken } from '@/utils/auth';
import Cookies from "js-cookie";

const LoginMessage: React.FC<{
  content: string;
}> = ({ content }) => (
  <Alert
    style={{
      marginBottom: 24,
    }}
    message={content}
    type="error"
    showIcon
  />
);

const Login: React.FC = () => {
  const [userLoginState, setUserLoginState] = useState<SYSTEM.LoginResult>({});
  const [type, setType] = useState<string>('account');
  const { initialState, setInitialState } = useModel('@@initialState');

  const loginFormRef = useRef<
    ProFormInstance<SYSTEM.LoginBody>
    >();

  // const [loginForm, setLoginForm] = useState<{username?: string, password?: string, rememberMe?: boolean}>({username: "", password: "", rememberMe: false});

  const [captchaOnOff, setCaptchaOnOff] = useState<boolean>(true);
  const [imgSrc, setImgSrc] = useState<string>("");
  const [codeUuid, setCodeUuid] = useState<string>("");
  const [captcha, setCaptcha] = useState<string>("");

  const intl = useIntl();

  const getCookies = () => {
    const username = Cookies.get("username");
    const password = Cookies.get("password");
    const phone = Cookies.get("phone");
    const rememberMe = Cookies.get('rememberMe');
    // setLoginForm({username, password, rememberMe: rememberMe === "true"});
    return {username, password, rememberMe: rememberMe === "true", phone};
  };

  const captchaImage = async () => {
    const resData = await getCaptchaImage();
    if (resData.code === 200) {
      const data = resData.data;
      if (!data.captchaOnOff) {
        setCaptchaOnOff(data.captchaOnOff);
      } else {
        setImgSrc(`data:image/gif;base64,${data.img}`);
        setCodeUuid(data.uuid);
      }
    }
  }

  useEffect(() => {
    captchaImage();
  }, []);

  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();
    if (userInfo) {
      await setInitialState((s) => ({
        ...s,
        currentUser: userInfo,
      }));
    }
  };

  const fetchMenuInfo = async () => {
    const menuDataInfo = await initialState?.fetchMenuInfo?.()
    if (menuDataInfo) {
      await setInitialState((s) => ({
        ...s,
        customMenuData: menuDataInfo,
      }));
    }
  }

  const handleSubmit = async (values: SYSTEM.LoginParams) => {
    if (!captcha) {
      message.error("请填写验证码");
      return;
    }
    try {
      if (values.autoLogin === true) {
        if (type === "account") {
          Cookies.set("username", values.username || "", { expires: 30 });
          Cookies.set("password", values.password || "", { expires: 30 });
        } else if (type === "mobile") {
          Cookies.set("phone", values.phone || "", { expires: 30 });
        }
        Cookies.set("rememberMe", "true", { expires: 30 });
      } else {
        Cookies.remove("username");
        Cookies.remove("password");
        Cookies.remove("phone");
        Cookies.remove("rememberMe");
      }
      // 登录
      const msg = await login({ ...values, code: captcha, uuid: codeUuid });
      if (msg.code === 200) {
        setToken(msg?.data?.token || '');
        const defaultLoginSuccessMessage = intl.formatMessage({
          id: 'pages.login.success',
          defaultMessage: '登录成功！',
        });
        message.success(defaultLoginSuccessMessage);
        await fetchUserInfo();
        // 请求默认菜单
        await fetchMenuInfo();
        /** 此方法会跳转到 redirect 参数所在的位置 */
        if (!history) return;
        const { query } = history.location;
        const { redirect } = query as { redirect: string };
        history.push(redirect || '/');
        return;
      }
      console.log(msg);
      // 如果失败去设置用户错误信息
      setUserLoginState(msg);
    } catch (error) {
      const defaultLoginFailureMessage = intl.formatMessage({
        id: 'pages.login.failure',
        defaultMessage: '登录失败，请重试！',
      });
      message.error(defaultLoginFailureMessage);
    }
  };
  const { code: status } = userLoginState;
  const loginType = 'account';
  return (
    <div className={styles.container}>
      {/*<div className={styles.lang} data-lang>
        {SelectLang && <SelectLang />}
      </div>*/}
      <div className={styles.content}>
        <LoginForm
          formRef={loginFormRef}
          logo={<img alt="logo" style={{borderRadius: 30}} src="/logo1.jpg" />}
          title="寻 路"
          subTitle={intl.formatMessage({ id: 'pages.layouts.userLayout.title' })}
          request={async ()=> {
            const data = getCookies();
            return {
              autoLogin: data.rememberMe,
              username: data.username,
              password: data.password,
              phone: data.phone
            }}}
          actions={[
            <FormattedMessage
              key="loginWith"
              id="pages.login.loginWith"
              defaultMessage="其他登录方式"
            />,
            <AlipayCircleOutlined key="AlipayCircleOutlined" className={styles.icon} />,
            <TaobaoCircleOutlined key="TaobaoCircleOutlined" className={styles.icon} />,
            <WeiboCircleOutlined key="WeiboCircleOutlined" className={styles.icon} />,
          ]}
          onFinish={async (values) => {
            await handleSubmit(values as SYSTEM.LoginParams);
          }}
        >
          <Tabs activeKey={type} onChange={setType}>
            <Tabs.TabPane
              key="account"
              tab={intl.formatMessage({
                id: 'pages.login.accountLogin.tab',
                defaultMessage: '账户密码登录',
              })}
            />
            <Tabs.TabPane
              key="mobile"
              tab={intl.formatMessage({
                id: 'pages.login.phoneLogin.tab',
                defaultMessage: '手机号登录',
              })}
            />
          </Tabs>

          { status && status !== 200 && loginType === 'account' && (
            <LoginMessage
              content={intl.formatMessage({
                id: 'pages.login.accountLogin.errorMessage',
                defaultMessage: '账户或密码错误(admin/admin123)',
              })}
            />
          )}
          {type === 'account' && (
            <>
              <ProFormText
                name="username"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.username.placeholder',
                  defaultMessage: '用户名: admin',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.username.required"
                        defaultMessage="请输入用户名!"
                      />
                    ),
                  },
                ]}
              />
              <ProFormText.Password
                name="password"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon} />,
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.password.placeholder',
                  defaultMessage: '密码: admin123',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.password.required"
                        defaultMessage="请输入密码！"
                      />
                    ),
                  },
                ]}
              />
            </>
          )}

          {status !== 200 && false && <LoginMessage content="验证码错误" />}
          {type === 'mobile' && (
            <>
              <ProFormText
                fieldProps={{
                  size: 'large',
                  prefix: <MobileOutlined className={styles.prefixIcon} />,
                }}
                name="phone"
                placeholder={intl.formatMessage({
                  id: 'pages.login.phoneNumber.placeholder',
                  defaultMessage: '手机号',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.phoneNumber.required"
                        defaultMessage="请输入手机号！"
                      />
                    ),
                  },
                  {
                    pattern: /^1\d{10}$/,
                    message: (
                      <FormattedMessage
                        id="pages.login.phoneNumber.invalid"
                        defaultMessage="手机号格式错误！"
                      />
                    ),
                  },
                ]}
              />
              <ProFormCaptcha
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon} />,
                }}
                captchaProps={{
                  size: 'large',
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.captcha.placeholder',
                  defaultMessage: '请输入验证码',
                })}
                captchaTextRender={(timing, count) => {
                  if (timing) {
                    return `${count} ${intl.formatMessage({
                      id: 'pages.getCaptchaSecondText',
                      defaultMessage: '获取验证码',
                    })}`;
                  }
                  return intl.formatMessage({
                    id: 'pages.login.phoneLogin.getVerificationCode',
                    defaultMessage: '获取验证码',
                  });
                }}
                name="captcha"
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.captcha.required"
                        defaultMessage="请输入验证码！"
                      />
                    ),
                  },
                ]}
                onGetCaptcha={async () => {
                  const mobile = loginFormRef.current?.getFieldsValue().phone || "";
                  if (!mobile) {
                    message.error("请填写手机号！");
                    throw new Error("请填写手机号！");
                  }
                  if (!captcha) {
                    message.error("请填写验证码！");
                    throw new Error("请填写验证码！");
                  }
                  const result = await getFakeCaptcha({phone: mobile, uuid: codeUuid, code: captcha});
                  if (result.code === 200) {
                    message.success(`获取验证码成功！验证码为：${result.data}`);
                    return;
                  } else {
                    throw new Error(result.msg);
                  }
                }}
              />
            </>
          )}
          {
            captchaOnOff && (
              <div className="ant-row ant-form-item" style={{rowGap: "0px"}}>
                <div className="ant-col ant-form-item-control">
                  <div className="ant-form-item-control-input">
                    <div className="ant-form-item-control-input-content">
                      <div style={{display: "flex", alignItems: "center"}}>
                    <span
                      className="ant-input-affix-wrapper ant-input-affix-wrapper-lg"
                      style={{flex: "1 1 0%", transition:" width 0.3s ease 0s", marginRight: "8px"}}><span
                      className="ant-input-prefix"></span>
                    <input id="captcha" placeholder="请输入验证码！" onChange={(target) => {
                      setCaptcha(target.target.value);
                    }} className="ant-input" type="text" value={captcha} />
                    </span>
                        <img src={imgSrc} style={{width: "100px", height: "40.14px", cursor: "pointer"}} onClick={captchaImage}/>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            )
          }

          <div
            style={{
              marginBottom: 24,
            }}
          >
            <ProFormCheckbox noStyle name="autoLogin">
              <FormattedMessage id="pages.login.rememberMe" defaultMessage="自动登录" />
            </ProFormCheckbox>
            <a
              style={{
                float: 'right',
              }}
              onClick={()=>{
                history.push("/user/resetPassword");
                return false;
              }}
            >
              <FormattedMessage id="pages.login.forgotPassword" defaultMessage="忘记密码" />
            </a>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Login;
