import React from 'react';
import {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormRadio,
  ProFormGroup, ModalForm,
} from '@ant-design/pro-form';
import {SYSTEM} from "@/services/system/typings";

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<SYSTEM.SysNotice>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysNotice>;
};

const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  return (
    <ModalForm
      title={"新增通知公告"}
      width="750px"
      visible={props.updateModalVisible}
      modalProps={{destroyOnClose: true}}
      onVisibleChange={props.onCancel}
      onFinish={props.onSubmit}
    >
      <ProFormGroup>
        <ProFormText
          initialValue={props.values.noticeTitle}
          rules={[
            {
              required: true,
              message: "公告标题不能为空",
            },
          ]}
          width="md"
          name="noticeTitle"
          label={"公告标题"}
        />
        <ProFormSelect
          initialValue={props.values.noticeType}
          rules={[
            {
              required: true,
              message: "公告类型不能为空",
            },
          ]}
          width="md"
          name="noticeType"
          label={"公告类型"}
          options={[
            {
              label: "通知",
              value: "1"
            },
            {
              label: "公告",
              value: "2"
            }
          ]}
        />
      </ProFormGroup>
      <ProFormRadio.Group
        initialValue={props.values.status}
        width={"md"}
        name="status"
        label="状态"
        options={[
          {
            label: '是',
            value: '0',
          },
          {
            label: '否',
            value: '1',
          }
        ]}
      />
      <ProFormTextArea width="md" name="noticeContent" label={"内容"} initialValue={props.values.noticeContent} />
    </ModalForm>
  );
};

export default UpdateForm;
