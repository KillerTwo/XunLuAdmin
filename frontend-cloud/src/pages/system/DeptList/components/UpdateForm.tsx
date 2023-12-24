import React from 'react';
import {
  ProFormText,
  ModalForm, ProForm, ProFormTreeSelect, ProFormRadio, ProFormDigit,
} from '@ant-design/pro-form';
import {SYSTEM} from "@/services/system/typings";
import {TreeSelect} from "antd";
import {sysDeptSelectList} from "@/services/system/sysDept";

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<SYSTEM.SysDept>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysDept>;
};



const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  return (
    <ModalForm
      modalProps={{destroyOnClose: true}}
      title={'修改菜单'}
      width="60%"
      visible={props.updateModalVisible}
      onVisibleChange={props.onCancel}
      onFinish={props.onSubmit}
    >
      <ProForm.Group>
        <ProFormTreeSelect
          initialValue={props.values.parentId}
          width={"md"}
          label="上级部门"
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
              message: "上级部门不能为空"
            }
          ]}
          name={"parentId"}
        />
      </ProForm.Group>
      <ProForm.Group>
        <ProFormText
          initialValue={props.values.deptName}
          rules={[
            {
              required: true,
              message: "部门名称必填",
            },
          ]}
          width="md"
          name="deptName"
          label={"部门名称"}
        />
        <ProFormDigit
          initialValue={props.values.orderNum}
          rules={[
            {
              required: true,
              message: "显示排序必填",
            },
          ]}
          width="md"
          name="orderNum"
          label={"显示排序"}
          min={0}
          max={999}
        />
      </ProForm.Group>
      <ProForm.Group>
        <ProFormText
          initialValue={props.values.leader}
          width="md"
          name="leader"
          label={"负责人"}
        />
        <ProFormText
          initialValue={props.values.phone}
          width="md"
          name="phone"
          label={"联系电话"}
        />
      </ProForm.Group>
      <ProForm.Group>
        <ProFormText
          initialValue={props.values.email}
          width="md"
          name="email"
          label={"邮箱"}
        />
        <ProFormRadio.Group
          initialValue={props.values.status}
          width={"md"}
          name="status"
          label="部门状态"
          options={[
            {
              label: '正常',
              value: '0',
            },
            {
              label: '停用',
              value: '1',
            }
          ]}
        />
      </ProForm.Group>
    </ModalForm>
  );
};

export default UpdateForm;
