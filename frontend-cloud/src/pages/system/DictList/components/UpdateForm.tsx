import React from 'react';
import {
  ProFormText,
  ProFormTextArea,
  ProFormRadio,
  ModalForm,
} from '@ant-design/pro-form';
import {SYSTEM} from "@/services/system/typings";

export type FormValueType = Partial<SYSTEM.SysDictType>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysDictType>;
};

const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  return (
    <ModalForm
      title="修改字典类型"
      width="600px"
      visible={props.updateModalVisible}
      onVisibleChange={props.onCancel}
      onFinish={props.onSubmit}
    >
      <ProFormText
        initialValue={props.values.dictName}
        rules={[
          {
            required: true,
            message: "字典名称不能为空",
          },
        ]}
        width="md"
        name="dictName"
        label="字典名称"
      />
      <ProFormText
        initialValue={props.values.dictType}
        rules={[
          {
            required: true,
            message: "字典类型不能为空",
          },
        ]}
        width="md"
        name="dictType"
        label="字典类型"
      />
      <ProFormRadio.Group
        initialValue={props.values.status}
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
      <ProFormTextArea initialValue={props.values.remark} width="md" name="remark" label="备注" />
    </ModalForm>
  );
};

export default UpdateForm;
