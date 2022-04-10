import React, {useRef, useState} from 'react';
import {Button, List, message, Modal} from 'antd';
import {SYSTEM} from "@/services/system/typings";

import {LoginForm, ProFormInstance, ProFormText} from "@ant-design/pro-form";
import {LockOutlined} from "@ant-design/icons";
import styles from "@/pages/user/ResetPassword/index.less";
import {getCaptchaImage, resetPassword} from "@/services/system/login";
import {history} from "@@/core/history";

type Unpacked<T> = T extends (infer U)[] ? U : T;
type PropsType = {
  currentUser: SYSTEM.SysUser
}

const passwordStrength = {
  strong: <span className="strong">强</span>,
  medium: <span className="medium">中</span>,
  weak: <span className="weak">弱 Weak</span>,
};

const SecurityView: React.FC<PropsType> = (props) => {

  const [captchaOnOff, setCaptchaOnOff] = useState<boolean>(true);
  const [imgSrc, setImgSrc] = useState<string>("");
  const [codeUuid, setCodeUuid] = useState<string>("");
  const [captcha, setCaptcha] = useState<string>("");
  const [passwordVisible, handlePasswordVisible] = useState<boolean>(false);

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

  const getData = () => [
    {
      title: '账户密码',
      description: (
        <>
          当前密码强度：
          {passwordStrength.strong}
        </>
      ),
      actions: [<a key="Modify" onClick={async () => {
        await captchaImage();
        handlePasswordVisible(true);
      }}>修改</a>],
    },
    {
      title: '密保手机',
      description: `已绑定手机：${props.currentUser?.phonenumber}`,
      actions: [<a key="Modify" onClick={() => {
        message.warn("请到个人中心修改！");
      }
      }>修改</a>],
    },
    /*{
      title: '密保问题',
      description: '未设置密保问题，密保问题可有效保护账户安全',
      actions: [<a key="Set">设置</a>],
    },*/
    {
      title: '备用邮箱',
      description: `已绑定邮箱：${props.currentUser?.email}`,
      actions: [<a key="Modify" onClick={() => {
        message.warn("请到个人中心修改！");
      }
      }>修改</a>],
    },
    /*{
      title: 'MFA 设备',
      description: '未绑定 MFA 设备，绑定后，可以进行二次确认',
      actions: [<a key="bind">绑定</a>],
    },*/
  ];

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

  const formRef = useRef<
    ProFormInstance<SYSTEM.ResetPasswordBody>
    >();

  const data = getData();
  return (
    <>
      <List<Unpacked<typeof data>>
        itemLayout="horizontal"
        dataSource={data}
        renderItem={(item) => (
          <List.Item actions={item.actions}>
            <List.Item.Meta title={item.title} description={item.description} />
          </List.Item>
        )}
      />
      <Modal visible={passwordVisible} width={"60%"} onCancel={() => handlePasswordVisible(false)} footer={false}>
      <LoginForm
        formRef={formRef}
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
      </LoginForm>
      </Modal>
    </>
  );
};

export default SecurityView;
