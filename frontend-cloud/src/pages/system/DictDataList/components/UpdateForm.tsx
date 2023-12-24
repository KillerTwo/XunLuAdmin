import React from 'react';
import {
  ProFormText,
  ProFormTextArea,
  ProFormRadio,
  ModalForm, ProFormDigit,
} from '@ant-design/pro-form';
import {SYSTEM} from "@/services/system/typings";

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<SYSTEM.SysDictData>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysDictData>;
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
        initialValue={props.values.dictType}
        width="md"
        name="dictType"
        label="字典类型"
        disabled={true}
      />
      <ProFormText
        initialValue={props.values.dictLabel}
        rules={[
          {
            required: true,
            message: "数据标签不能为空",
          },
        ]}
        width="md"
        name="dictLabel"
        label="数据标签"
      />
      <ProFormText
        initialValue={props.values.dictValue}
        rules={[
          {
            required: true,
            message: "数据键值不能为空",
          },
        ]}
        width="md"
        name="dictValue"
        label="数据键值"
      />
      <ProFormText
        initialValue={props.values.cssClass}
        width="md"
        name="cssClass"
        label="样式属性"
      />
      <ProFormDigit
        initialValue={props.values.dictSort}
        rules={[
          {
            required: true,
            message: "字典顺序不能为空",
          },
        ]}
        width="md"
        name="dictSort"
        label={"字典顺序"}
        min={0}
        max={100}
      />
      <ProFormText
        initialValue={props.values.listClass}
        width="md"
        name="listClass"
        label="回显样式"
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
