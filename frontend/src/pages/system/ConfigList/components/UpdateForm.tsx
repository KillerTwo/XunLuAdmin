import React from 'react';
import {
  ProFormText,
  ProFormTextArea,
  ProFormRadio,
  ModalForm,
} from '@ant-design/pro-form';
import {SYSTEM} from "@/services/system/typings";

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<SYSTEM.SysConfig>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysConfig>;
};

const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  return (
    <ModalForm
      title={"新增"}
      width="600px"
      modalProps={{destroyOnClose: true}}
      visible={props.updateModalVisible}
      onVisibleChange={props.onCancel}
      onFinish={props.onSubmit}
    >
      <ProFormText
        initialValue={props.values.configName}
        rules={[
          {
            required: true,
            message: "参数名称不能为空",
          },
        ]}
        width="md"
        name="configName"
        label={"参数名称"}
      />
      <ProFormText
        initialValue={props.values.configKey}
        rules={[
          {
            required: true,
            message: "参数键名不能为空",
          },
        ]}
        width="md"
        name="configKey"
        label={"参数键名"}
      />
      <ProFormText
        initialValue={props.values.configValue}
        rules={[
          {
            required: true,
            message: "参数键值不能为空",
          },
        ]}
        width="md"
        name="configValue"
        label={"参数键值"}
      />
      <ProFormRadio.Group
        initialValue={props.values.configType}
        width={"md"}
        name="configType"
        label="系统内置"
        options={[
          {
            label: '是',
            value: 'Y',
          },
          {
            label: '否',
            value: 'N',
          }
        ]}
      />
      <ProFormTextArea initialValue={props.values.remark} width="md" name="remark" label={"备注"} />
    </ModalForm>
  );
};

export default UpdateForm;
