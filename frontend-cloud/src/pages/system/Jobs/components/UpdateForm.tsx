import React from 'react';
import {
  ProFormText,
  ProFormRadio,
  ModalForm, ProFormSelect,
} from '@ant-design/pro-form';
import {SYSTEM} from "@/services/system/typings";
import {useDict} from "@/hooks/Dict";

export type FormValueType = Partial<SYSTEM.SysJob>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysJob>;
};

const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  const jobGroupOptions = useDict("sys_job_group");
  return (
    <ModalForm
      title="修改字典类型"
      width="600px"
      visible={props.updateModalVisible}
      onVisibleChange={props.onCancel}
      onFinish={props.onSubmit}
    >
      <ProFormText
        initialValue={props.values.jobName}
        rules={[
          {
            required: true,
            message: "任务名称不能为空",
          },
        ]}
        width="md"
        name="jobName"
        label="任务名称"
      />
      <ProFormSelect
        initialValue={props.values.jobGroup}
        width={"md"}
        name={"jobGroup"}
        label={"任务分组"}
        options={jobGroupOptions}
      />
      <ProFormText
        initialValue={props.values.invokeTarget}
        rules={[
          {
            required: true,
            message: "调用方法不能为空",
          },
        ]}
        width="md"
        name="invokeTarget"
        label="调用方法"
      />
      <ProFormText
        initialValue={props.values.cronExpression}
        rules={[
          {
            required: true,
            message: "cron执行表达式不能为空",
          },
        ]}
        width="md"
        name="cronExpression"
        label="cron执行表达式"
      />
      <ProFormRadio.Group
        initialValue={props.values.misfirePolicy}
        name="misfirePolicy"
        label="执行策略"
        radioType="button"
        options={[
          {
            label: '立即执行',
            value: '1',
          },
          {
            label: '执行一次',
            value: '2',
          },
          {
            label: '放弃执行',
            value: '3',
          },
        ]}
      />
      <ProFormRadio.Group
        initialValue={props.values.concurrent}
        name="concurrent"
        label="是否并发"
        radioType="button"
        options={[
          {
            label: '允许',
            value: '0',
          },
          {
            label: '禁止',
            value: '1',
          }
        ]}
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
    </ModalForm>
  );
};

export default UpdateForm;
