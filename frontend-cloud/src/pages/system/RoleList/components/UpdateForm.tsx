import React from 'react';
import {
  ProFormText,
  ProFormTextArea,
  ProForm, ProFormDigit, ModalForm, ProFormRadio, ProFormTreeSelect
} from '@ant-design/pro-form';
import {SYSTEM} from "@/services/system/typings";
import {sysMenuSelectList} from "@/services/system/sysMenu";
import {TreeSelect} from "antd";

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<SYSTEM.SysRole>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysRole>;
};

const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  const currentData = props.values;
  return (
    <ModalForm
      modalProps={{destroyOnClose: true}}
      title={"新增角色"}
      width="60%"
      visible={props.updateModalVisible}
      onVisibleChange={props.onCancel}
      onFinish={props.onSubmit}
    >
      <ProForm.Group>
        <ProFormText
          initialValue={currentData.roleName}
          rules={[
            {
              required: true,
              message: "角色名称不能为空",
            },
          ]}
          label={"角色名称"}
          width="md"
          name="roleName"
        />
        <ProFormText
          initialValue={currentData.roleKey}
          rules={[
            {
              required: true,
              message: "角色标识不能为空",
            },
          ]}
          label={"角色标识"}
          width="md"
          name="roleKey"
        />
      </ProForm.Group>

      <ProForm.Group>
        <ProFormDigit width="md" name="roleSort" label="角色顺序" initialValue={currentData.roleSort} />
        <ProFormRadio.Group
          initialValue={currentData.status}
          width={"md"}
          name="status"
          label="状态"
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
      <ProForm.Group>
        <ProFormTreeSelect
          initialValue={currentData.menuIds}
          width={"md"}
          label="菜单权限"
          request={async () => {
            const resData = await sysMenuSelectList();
            return resData.data;
          }}
          fieldProps={{
            fieldNames: {
              label: 'label',
              value: 'id',
              children: 'children'
            },
            showCheckedStrategy: TreeSelect.SHOW_ALL,
            treeCheckable: true
          }}
          name={"menuIds"}
        />
      </ProForm.Group>
      <ProForm.Group>
        <ProFormTextArea width="md" label={"备注"} name="remark" initialValue={currentData.remark} />
      </ProForm.Group>
    </ModalForm>
  );
};

export default UpdateForm;
