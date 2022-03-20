import React from 'react';
import {TreeSelect} from 'antd';
import {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProForm, ProFormTreeSelect, ModalForm,
} from '@ant-design/pro-form';
import { useIntl, FormattedMessage } from 'umi';
import {SYSTEM} from "@/services/system/typings";
import {sysDeptSelectList} from "@/services/system/sysDept";
import {sysPostSelectList} from "@/services/system/sysPost";
import {sysRoleSelectList} from "@/services/system/sysRole";

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<SYSTEM.SysUser>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysUser>;
};

const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  const intl = useIntl();
  const currentUser = props.values;
  console.log("currentRow: ", currentUser);
  return (
    <ModalForm
      title={intl.formatMessage({
        id: 'pages.system.user.searchTable.createForm.newUser',
        defaultMessage: '修改用户',
      })}
      {
        ...{
          labelCol: { span: 6 },
          wrapperCol: { span: 18 },
        }
      }
      width="60%"
      layout={'horizontal'}
      visible={props.updateModalVisible}
      onFinish={props.onSubmit}
      onVisibleChange={props.onCancel}
    >
      <ProForm.Group>
        <ProFormText
          initialValue={currentUser.userName}
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id="pages.system.user.searchTable.rule.username"
                  defaultMessage="username is required"
                />
              ),
            },
          ]}
          width="md"
          name="userName"
          label={intl.formatMessage({
            id: 'pages.system.user.searchTable.username',
            defaultMessage: '用户名称',
          })}
        />
        <ProFormText
          initialValue={currentUser.nickName}
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id="pages.system.user.searchTable.rule.nickName"
                  defaultMessage="nick name is required"
                />
              ),
            },
          ]}
          width="md"
          name="nickName"
          label={intl.formatMessage({
            id: 'pages.system.user.searchTable.nickname',
            defaultMessage: '用户昵称',
          })}
        />
      </ProForm.Group>
      <ProForm.Group>
        <ProFormText
          initialValue={currentUser.phonenumber}
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id="pages.system.user.searchTable.rule.mobile"
                  defaultMessage="mobile is required"
                />
              ),
            },
          ]}
          width="md"
          name="phonenumber"
          label={intl.formatMessage({
            id: 'pages.system.user.searchTable.mobile',
            defaultMessage: '用户手机号',
          })}
        />
        <ProFormText
          initialValue={currentUser.email}
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id="pages.system.user.searchTable.rule.email"
                  defaultMessage="email is required"
                />
              ),
            },
            {
              type: 'email',
              message: '邮箱格式不合法!',
            },
          ]}
          width="md"
          name="email"
          label={intl.formatMessage({
            id: 'pages.system.user.searchTable.email',
            defaultMessage: '用户邮箱',
          })}
        />
      </ProForm.Group>
      <ProForm.Group>
        <ProFormTreeSelect
          initialValue={currentUser.deptId}
          width={"md"}
          label="所属部门"
          request={async () => {
            const resData = await sysDeptSelectList();
            return resData.data;
          }}
          fieldProps={{
            fieldNames: {
              label: 'label',
              value: 'id',
              children: 'children'
            },
            showCheckedStrategy: TreeSelect.SHOW_PARENT
          }}
          rules={[
            {
              required: true,
              message: "所属部门不能为空"
            }
          ]}
          name={"deptId"}
        />
        <ProFormSelect
          initialValue={currentUser.postIds}
          request={async () => {
            const resData = await sysPostSelectList();
            return resData.data.map((ele: { postId: number; postName: string; }) => {
              return {
                value: ele.postId,
                label: ele.postName
              }
            })
          }}
          mode={"multiple"}
          width="md"
          name="postIds"
          label="所在岗位"
          rules={[
            {
              required: true,
              message: "所属岗位不能为空"
            }
          ]}
        />
      </ProForm.Group>
      <ProForm.Group>
        <ProFormSelect
          initialValue={currentUser.roleIds}
          request={async () => {
            const resData = await sysRoleSelectList();
            return resData.data.map((ele: { roleId: any; roleName: any; }) => {
              return {
                value: ele.roleId,
                label: ele.roleName
              }
            })
          }}
          mode={"multiple"}
          width="md"
          name="roleIds"
          label="所属角色"
          rules={[
            {
              required: true,
              message: "所属角色不能为空"
            }
          ]}
        />
        <ProFormSelect
          initialValue={currentUser.sex}
          options={[
            {
              value: '1',
              label: '男',
            },
            {
              value: '0',
              label: '女',
            },
          ]}
          width="md"
          name="sex"
          label="用户性别"
          rules={[
            {
              required: true,
              message: "用户性别不能为空"
            }
          ]}
        />
      </ProForm.Group>
      <ProForm.Group>
        <ProFormTextArea initialValue={currentUser.remark} width="md" label="用户备注" name="remark" />
      </ProForm.Group>
    </ModalForm>
  );
};

export default UpdateForm;
