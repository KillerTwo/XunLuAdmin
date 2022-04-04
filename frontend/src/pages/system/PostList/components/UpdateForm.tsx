import React from 'react';
import {
  ProFormText,
  ProFormTextArea,
  ProFormRadio,
  ProFormDigit, ModalForm,
} from '@ant-design/pro-form';
import {SYSTEM} from "@/services/system/typings";

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<SYSTEM.SysPost>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysPost>;
};

const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  return (
    <ModalForm
      title={"修改岗位"}
      width="400px"
      visible={props.updateModalVisible}
      onVisibleChange={props.onCancel}
      onFinish={props.onSubmit}
    >
      <ProFormText
        initialValue={props.values.postName}
        rules={[
          {
            required: true,
            message: "岗位名称不能为空",
          },
        ]}
        width="md"
        name="postName"
        label={"岗位编码"}
      />
      <ProFormText
        initialValue={props.values.postCode}
        rules={[
          {
            required: true,
            message: "岗位编码不能为空",
          },
        ]}
        width="md"
        name="postCode"
        label={"岗位编码"}
      />
      <ProFormDigit
        initialValue={props.values.postSort}
        rules={[
          {
            required: true,
            message: "岗位顺序不能为空",
          },
        ]}
        width="md"
        name="postSort"
        label={"岗位顺序"}
        min={0}
        max={1}
      />
      <ProFormRadio.Group
        initialValue={props.values.status}
        width={"md"}
        name="status"
        label="岗位状态"
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
      <ProFormTextArea initialValue={props.values.remark} width="md" name="remark" label={"备注"} />
    </ModalForm>
  );
};

export default UpdateForm;
