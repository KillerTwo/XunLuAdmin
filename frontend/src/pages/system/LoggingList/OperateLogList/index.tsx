import {DeleteOutlined} from '@ant-design/icons';
import {Button, message, Drawer, Tag, Space, Popconfirm} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import {cleanOperlog, removeOperlog, sysOperlogList} from "@/services/system/operlog";
import {SYSTEM} from "@/services/system/typings";


/**
 * @en-US Update node
 * @zh-CN 更新节点
 *
 * @param fields
 */
const handleClean = async () => {
  const hide = message.loading('Configuring');
  try {
    await cleanOperlog();
    hide();

    message.success('Configuration is successful');
    return true;
  } catch (error) {
    hide();
    message.error('Configuration failed, please try again!');
    return false;
  }
};

/**
 *  Delete node
 * @zh-CN 删除节点
 *
 * @param selectedRows
 */
const handleRemove = async (selectedRows: SYSTEM.Operlog[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeOperlog([
    ...selectedRows.map((row) => row.operId),
    ]);
    hide();
    message.success('删除成功');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请稍后重试！');
    return false;
  }
};

const OperateLogList: React.FC = () => {

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<SYSTEM.Operlog>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.Operlog[]>([]);

  const columns: ProColumns<SYSTEM.Operlog>[] = [
    {
      title: "日志编号",
      dataIndex: 'operId',
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
      title: "系统模块",
      dataIndex: 'title',
    },
    {
      title: "操作类型",
      dataIndex: 'operatorType',
      valueType: "select"
    },
    {
      title: "请求方式",
      dataIndex: 'requestMethod',
      hideInSearch: true
    },
    {
      title: "操作人员",
      dataIndex: 'operName',
    },
    {
      title: "操作地址",
      dataIndex: 'operIp',
      hideInSearch: true
    },
    {
      title: "操作地点",
      dataIndex: 'operLocation',
      hideInSearch: true
    },
    {
      title: "操作状态",
      dataIndex: 'status',
      hideInForm: true,
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
            <Tag color={"green"}>
              失败
            </Tag>
          ),
          status: 'Processing',
        }
      },
    },
    {
      title: "操作日期",
      sorter: true,
      dataIndex: 'operTime',
      valueType: 'dateTime',
      hideInSearch: true
    },
    {
      title: "操作方法",
      dataIndex: 'method',
      hideInSearch: true,
      hideInForm: true,
      hideInTable: true
    },
    {
      title: "请求参数",
      dataIndex: 'operParam',
      hideInSearch: true,
      hideInForm: true,
      hideInTable: true
    },
    {
      title: "返回参数",
      dataIndex: 'jsonResult',
      hideInSearch: true,
      hideInForm: true,
      hideInTable: true,
    },
    /*{
      title: "操作",
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="config"
          onClick={() => {
            handleModalVisible(true);
            setCurrentRow(record);
          }}
        >
          详细
        </a>
      ],
    },*/
  ];

  return (
    <PageContainer>
      <ProTable<SYSTEM.Operlog, SYSTEM.PageParams>
        headerTitle={"操作日志列表"}
        actionRef={actionRef}
        rowKey="operId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Popconfirm
            title="确定要清空操作日志?"
            onConfirm={async () => {
              await handleClean();
              actionRef.current?.reloadAndRest?.();
            }}
            onCancel={() => {
            }}
            okText="确定"
            cancelText="取消"
          >
            <Button
              type="primary"
              key="primary"
            >
              <DeleteOutlined /> 清空
            </Button>,
          </Popconfirm>

        ]}
        request={sysOperlogList}
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
                title="确定要删除操作日志?"
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
        {currentRow?.title && (
          <ProDescriptions<SYSTEM.Operlog>
            column={2}
            title={currentRow?.title}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.title,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.Operlog>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default OperateLogList;
