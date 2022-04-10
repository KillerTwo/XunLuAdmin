import {
  LockOutlined,
  MobileOutlined,
} from '@ant-design/icons';
import { message, Button } from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {ProFormCaptcha, ProFormText, LoginForm, ProFormInstance} from '@ant-design/pro-form';
import { useIntl, history, FormattedMessage } from 'umi';
import Footer from '@/components/Footer';
import {getCaptchaImage, getFakeCaptcha, resetPassword} from '@/services/system/login'

import styles from './index.less';
import {SYSTEM} from "@/services/system/typings";

const ResetPassword: React.FC = () => {
  const formRef = useRef<
    ProFormInstance<SYSTEM.ResetPasswordBody>
    >();

  const [captchaOnOff, setCaptchaOnOff] = useState<boolean>(true);
  const [imgSrc, setImgSrc] = useState<string>("");
  const [codeUuid, setCodeUuid] = useState<string>("");
  const [captcha, setCaptcha] = useState<string>("");

  const intl = useIntl();

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

  const handleResetPassword = async (values: SYSTEM.ResetPasswordBody) => {
    if (!captcha) {
      message.error("请填写验证码");
      return;
    }
    try {
      // 重置密码
      const msg = await resetPassword({ ...values, code: captcha, uuid: codeUuid });
      if (msg.code === 200) {
        message.success("重置密码成功");
        history.push("/user/login");
        return;
      }
      // message.error(msg.msg);
    } catch (error) {
      message.error("重置密码失败，请重试！");
    }
  };
  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <LoginForm
          formRef={formRef}
          logo={<img alt="logo" style={{borderRadius: 30}} src="/logo1.jpg" />}
          title="寻 路"
          subTitle={intl.formatMessage({ id: 'pages.layouts.userLayout.title' })}
          submitter={{
            render: () => {
              return <Button type="primary" style={{width: "328px"}} size={"large"} onClick={async () => {
                await handleResetPassword(formRef.current?.getFieldsValue() as SYSTEM.ResetPasswordBody);
              }
              }>
                提交
              </Button>
            }
          }}
        >
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
                  const mobile = formRef.current?.getFieldsValue().phone || "";
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
          <ProFormText.Password
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined className={styles.prefixIcon} />,
            }}
            name="password"
            placeholder={"新密码"}
            rules={[
              {
                required: true,
                message: "请输入新密码"
              }
            ]}
          />
          <ProFormText.Password
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined className={styles.prefixIcon} />,
            }}
            name="rePassword"
            placeholder={"验证密码"}
            dependencies={["password"]}
            hasFeedback
            rules={[
              {
                required: true,
                message: "请输入验证密码",
              },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('password') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('两次密码不一致!'));
                },
              }),
            ]}
          />
          <div
            style={{
              marginBottom: 24,
            }}
          >
            <a
              style={{
                float: 'left',
              }}
              onClick={()=>{
                setCaptcha("");
                formRef.current?.resetFields();
              }}
            >
              重置
            </a>
            <a
              style={{
                float: 'right',
              }}
              onClick={()=> {
                history.push("/user/login");
              }}
            >
              去登录
            </a>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default ResetPassword;
