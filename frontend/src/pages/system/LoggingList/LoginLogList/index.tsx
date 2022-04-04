import {DeleteOutlined} from '@ant-design/icons';
import {Button, message, Drawer, Tag, Space, Popconfirm} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import {SYSTEM} from "@/services/system/typings";
import {cleanLogininfor, removeLogininfor, sysLogininforList} from "@/services/system/logininfor";


/**
 * @en-US Update node
 * @zh-CN 更新节点
 *
 * @param fields
 */
const handleClean = async () => {
  const hide = message.loading('Configuring');
  try {
    await cleanLogininfor();
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
const handleRemove = async (selectedRows: SYSTEM.SysLogininfor[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeLogininfor([
      ...selectedRows.map((row) => row.infoId),
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

const LoginLogList: React.FC = () => {

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysLogininfor>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysLogininfor[]>([]);

  const columns: ProColumns<SYSTEM.SysLogininfor>[] = [
    {
      title: "访问编号",
      dataIndex: 'infoId',
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
      title: "用户名称",
      dataIndex: 'userName',
    },
    {
      title: "登录地址",
      dataIndex: 'ipaddr',
    },
    {
      title: "登录地点",
      dataIndex: 'loginLocation',
      hideInSearch: true
    },
    {
      title: "浏览器",
      dataIndex: 'browser',
      hideInSearch: true
    },
    {
      title: "操作系统",
      dataIndex: 'os',
      hideInSearch: true
    },
    {
      title: "登录状态",
      dataIndex: 'status',
      hideInForm: true,
      valueEnum: {
        "0": {
          text: (
            <Tag color={"blue"}>
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
        }
      },
    },
    {
      title: "操作信息",
      dataIndex: 'msg',
      hideInSearch: true
    },
    {
      title: "登录日期",
      sorter: true,
      dataIndex: 'loginTime',
      valueType: 'dateTime',
      hideInSearch: true
    }
  ];

  return (
    <PageContainer>
      <ProTable<SYSTEM.SysLogininfor, SYSTEM.PageParams>
        headerTitle={"登录日志列表"}
        actionRef={actionRef}
        rowKey="infoId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Popconfirm
            title="确定要清空登录日志?"
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
        request={sysLogininforList}
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
                title="确定要删除登录日志?"
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
        {currentRow?.userName && (
          <ProDescriptions<SYSTEM.SysLogininfor>
            column={2}
            title={currentRow?.userName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.infoId,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysLogininfor>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default LoginLogList;
