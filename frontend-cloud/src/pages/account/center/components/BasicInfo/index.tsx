import {message} from 'antd';
import React, {useEffect, useRef} from 'react';

import {
  ProForm,
  ProFormInstance,
  ProFormText,
  ProFormRadio
} from "@ant-design/pro-form";
import {SYSTEM} from "@/services/system/typings";
import {useDict} from "@/hooks/Dict";
import {updateUser} from "@/services/system/sysUser";

type PropsType = {
  currentUser: SYSTEM.SysUser;
}

const BasicInfo: React.FC<PropsType> = (props) => {

  const formRef = useRef<ProFormInstance<SYSTEM.SysUser>>();

  const dictOptions = useDict("sys_user_sex");

  useEffect(() => {
    console.log("props", props.currentUser);
  })

  return (
    <ProForm<SYSTEM.SysUser>
      onFinish={async (values) => {
        // await waitTime(2000);
        const resData = await updateUser({...props.currentUser, ...values});
        if (resData.code === 200) {
          message.success('更新成功！');
        }
      }}
      formRef={formRef}
      params={{ id: '100' }}
      formKey="base-form-use-demo"
      dateFormatter={(value, valueType) => {
        console.log('---->', value, valueType);
        return value.format('YYYY-MM-DD HH:mm:ss');
      }}
      autoFocusFirstInput
    >
      <ProForm.Group>
        <ProFormText
          initialValue={props.currentUser?.nickName}
          width="md"
          name="nickName"
          required
          label="用户昵称"
          placeholder="请输入名称"
          rules={[{ required: true, message: '这是必填项' }]}
        />
        <ProFormText
          initialValue={props.currentUser?.phonenumber}
          width="md"
          name="phonenuber"
          label="手机号码"
          placeholder="请输入手机号码"
          required
          rules={[{ required: true, message: '这是必填项' }, {
            pattern: /^1\d{10}$/,
            message: "手机号格式错误",
          },]}
        />
      </ProForm.Group>
      <ProForm.Group>
        <ProFormText
          initialValue={props.currentUser?.email}
          width="md"
          name="email"
          label="邮箱"
          placeholder="请输入邮箱"
        />
        <ProFormRadio.Group
          initialValue={props.currentUser?.sex}
          width="md"
          name="sex"
          label="性别"
          valueEnum={() => {
            const data = {}
             dictOptions.forEach((ele: {value?: string; label?: string}) => {
               data[ele.value||""] = ele.label
             })
            return data;
          }}
        />
      </ProForm.Group>
    </ProForm>
  );
};

export default BasicInfo;
