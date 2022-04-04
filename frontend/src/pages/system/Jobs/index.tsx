import {DeleteOutlined, EditOutlined, PlusOutlined, PicLeftOutlined, PlayCircleOutlined} from '@ant-design/icons';
import {Button, message, Drawer, Tag, Popconfirm, Space} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {ModalForm, ProFormRadio, ProFormSelect, ProFormText} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {SYSTEM} from "@/services/system/typings";
import {addSysJob, removeSysJob, runJob, sysJobList, updateSysJob} from "@/services/system/sysJob";
import {useDict} from "@/hooks/Dict"
import {history} from "umi";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: SYSTEM.SysJob) => {
  const hide = message.loading('正在添加');
  try {
    await addSysJob({ ...fields });
    hide();
    message.success('添加成功！');
    return true;
  } catch (error) {
    hide();
    message.error('添加失败，请稍后重试!');
    return false;
  }
};

/**
 * @en-US Update node
 * @zh-CN 更新节点
 *
 * @param fields
 */
const handleUpdate = async (fields: FormValueType) => {
  const hide = message.loading('修改中');
  try {
    await updateSysJob({...fields});
    hide();

    message.success('修改成功！');
    return true;
  } catch (error) {
    hide();
    message.error('修改失败，请稍后重试!');
    return false;
  }
};

/**
 *  Delete node
 * @zh-CN 删除节点
 *
 * @param selectedRows
 */
const handleRemove = async (selectedRows: SYSTEM.SysJob[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeSysJob([...selectedRows.map((row) => row.jobId),
    ]);
    hide();
    message.success('删除成功！');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请稍后重试！');
    return false;
  }
};

const JobList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysJob>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysJob[]>([]);
  const jobGroupOptions = useDict("sys_job_group");

  const columns: ProColumns<SYSTEM.SysJob>[] = [
    {
      title: "任务编号",
      dataIndex: 'jobId',
      hideInSearch: true,
      render: (dom, entity) => {
        return (
          <a
            onClick={() => {
              setCurrentRow(entity);
              setShowDetail(true);
            }}
          >
            {dom}
          </a>
        );
      },
    },
    {
      title: "任务名称",
      dataIndex: 'jobName',
    },
    {
      title: "任务组名",
      dataIndex: 'jobGroup',
      valueEnum: () => {
        const enumObj = {}
        jobGroupOptions.forEach((ele: {value: any, label: any}) => {
          enumObj[ele.value] = {text: ele.label, status: "Success"}
        })
        return enumObj;
      }
    },
    {
      title: "调用目标字符串",
      dataIndex: 'invokeTarget',
      hideInSearch: true
    },
    {
      title: "cron执行表达式",
      dataIndex: 'cronExpression',
      hideInSearch: true
    },
    {
      title: "状态",
      dataIndex: 'status',
      hideInForm: true,
      key: "status",
      valueEnum: {
        "0": {
          text: (
            <Tag color={"green"}>
              正常
            </Tag>
          ),
          status: 'Default',
        },
        "1": {
          text: (
            <Tag color={"red"}>
              停用
            </Tag>
          ),
          status: 'Processing',
        },
      },
    },
    {
      title: "操作",
      dataIndex: 'option',
      valueType: 'option',
      key: "option",
      render: (_, record) => [
        <a
          key="config"
          onClick={() => {
            handleUpdateModalVisible(true);
            setCurrentRow(record);
          }}
        >
          <EditOutlined />修改
        </a>,
        <Popconfirm
          title="确定要删除定时任务?"
          onConfirm={async () => {
            await handleRemove([record]);
            actionRef.current?.reloadAndRest?.();
          }}
          onCancel={() => {
          }}
          okText="确定"
          cancelText="取消"
        >
          <a>
            <DeleteOutlined />删除
          </a>
        </Popconfirm>,
        <Popconfirm
          title="确定要执行定时任务?"
          onConfirm={async () => {
            const resData = await runJob(record.jobId || 0, record.jobGroup || "");
            if (resData.code === 200) {
              message.success("执行成功！")
            }
          }}
          onCancel={() => {
          }}
          okText="确定"
          cancelText="取消"
        >
          <a
            key="runJob"
          >
            <PlayCircleOutlined />执行一次
          </a>,
        </Popconfirm>,
        <a
          key="logs"
          onClick={() => {
            setCurrentRow(record);
            history.push({
              pathname: "/system/sysJobLog",
              query: {
                jobName: record.jobName || "",
                jobGroup: record.jobGroup || ""
              }
            })
          }}
        >
          <PicLeftOutlined />调度日志
        </a>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<SYSTEM.SysJob, SYSTEM.PageParams>
        headerTitle="定时任务列表"
        actionRef={actionRef}
        rowKey="jobId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalVisible(true);
            }}
          >
            <PlusOutlined /> 新增
          </Button>,
        ]}
        request={sysJobList}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
        tableAlertRender={({ selectedRowKeys, onCleanSelected }) => (
          <Space size={24}>
          <span>
            已选 {selectedRowKeys.length} 项
            <a style={{ marginLeft: 8 }} onClick={onCleanSelected}>
              取消选择
            </a>
          </span>
          </Space>
        )}
        tableAlertOptionRender={() => {
          return (
            <Space size={16}>
              <Popconfirm
                title="确定要删除定时任务?"
                onConfirm={async () => {
                  await handleRemove(selectedRowsState);
                  setSelectedRows([]);
                  actionRef.current?.reloadAndRest?.();
                }}
                onCancel={() => {
                }}
                okText="确定"
                cancelText="取消"
              >
                <Button>
                  批量删除
                </Button>
              </Popconfirm>

            </Space>
          );
        }}
      />
      <ModalForm
        title="新增定时任务"
        width="600px"
        visible={createModalVisible}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as SYSTEM.SysDictType);
          if (success) {
            handleModalVisible(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormText
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
          width={"md"}
          name={"jobGroup"}
          label={"任务分组"}
          options={jobGroupOptions}
        />
        <ProFormText
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
          width={"md"}
          initialValue={"0"}
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
      <UpdateForm
        onSubmit={async (value) => {
          const success = await handleUpdate({...currentRow, ...value});
          if (success) {
            handleUpdateModalVisible(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={(value) => {
          if (!value) {
            handleUpdateModalVisible(false);
            if (!showDetail) {
              setCurrentRow(undefined);
            }
          }

        }}
        updateModalVisible={updateModalVisible}
        values={currentRow || {}}
      />

      <Drawer
        width={600}
        visible={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.jobName && (
          <ProDescriptions<SYSTEM.SysJob>
            column={2}
            title={currentRow?.jobName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.jobId,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysJob>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default JobList;
