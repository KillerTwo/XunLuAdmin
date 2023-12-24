import {Button, message, Drawer, Tag, Popconfirm, Space} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import {SYSTEM} from "@/services/system/typings";
import {useDict} from "@/hooks/Dict"
import {removeSysJobLog, sysJobLogList} from "@/services/system/sysJobLog";
import {history} from "umi";

/**
 *  Delete node
 * @zh-CN 删除节点
 *
 * @param selectedRows
 */
const handleRemove = async (selectedRows: SYSTEM.SysJobLog[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeSysJobLog([...selectedRows.map((row) => row.jobLogId),
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

const JobLogList: React.FC = () => {

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysJobLog>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysJobLog[]>([]);
  const jobGroupOptions = useDict("sys_job_group");

  const columns: ProColumns<SYSTEM.SysJobLog>[] = [
    {
      title: "日志编号",
      dataIndex: 'jobLogId',
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
      title: "日志信息",
      dataIndex: 'jobMessage',
      hideInSearch: true
    },
    {
      title: "执行状态",
      dataIndex: 'status',
      hideInForm: true,
      key: "status",
      valueEnum: {
        "0": {
          text: (
            <Tag color={"green"}>
              成功
            </Tag>
          ),
          status: 'Default',
        },
        "1": {
          text: (
            <Tag color={"red"}>
              失败
            </Tag>
          ),
          status: 'Processing',
        },
      },
    },
    {
      title: "执行时间",
      dataIndex: 'createTime',
      hideInSearch: true
    }
  ];

  return (
    <PageContainer>
      <ProTable<SYSTEM.SysJobLog, SYSTEM.PageParams>
        headerTitle="定时任务日志列表"
        actionRef={actionRef}
        rowKey="jobLogId"
        search={{
          labelWidth: 120,
        }}
        request={async (params) => {
          const {jobName, jobGroup} = history.location.query as {jobName: string, jobGroup: string};
          return await sysJobLogList({...params, jobName: jobName, jobGroup: jobGroup});
        }}
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
                title="确定要删除定时任务日志?"
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
          <ProDescriptions<SYSTEM.SysDictType>
            column={2}
            title={currentRow?.jobName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.jobLogId,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysJobLog>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default JobLogList;
