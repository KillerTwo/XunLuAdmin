import {DeleteOutlined, EditOutlined} from '@ant-design/icons';
import {Button, message, Drawer, Tag, Popconfirm, Space} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {ModalForm, ProFormGroup, ProFormRadio, ProFormSelect, ProFormText, ProFormTextArea} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {addSysNotice, removeSysNotice, sysNoticeList, updateSysNotice} from "@/services/system/sysNotice";
import {SYSTEM} from "@/services/system/typings";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: SYSTEM.SysNotice) => {
  const hide = message.loading('正在添加');
  try {
    await addSysNotice({ ...fields });
    hide();
    message.success('新增成功！');
    return true;
  } catch (error) {
    hide();
    message.error('新增失败，请稍后重试!');
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
  const hide = message.loading('更新中');
  try {
    await updateSysNotice({
      ...fields
    });
    hide();

    message.success('更新成功！');
    return true;
  } catch (error) {
    hide();
    message.error('更新失败，请稍后重试!');
    return false;
  }
};

/**
 *  Delete node
 * @zh-CN 删除节点
 *
 * @param selectedRows
 */
const handleRemove = async (selectedRows: SYSTEM.SysNotice[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeSysNotice([
    ...selectedRows.map((row) => row.noticeId),
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

const NoticeList: React.FC = () => {
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
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysNotice>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysNotice[]>([]);

  const columns: ProColumns<SYSTEM.SysNotice>[] = [
    {
      title: "序号",
      dataIndex: 'noticeId',
      hideInSearch: true
    },
    {
      title: "公告标题",
      dataIndex: 'noticeTitle',
    },
    {
      title: "公告类型",
      dataIndex: 'noticeType',
      hideInForm: true,
      valueType: "select",
      valueEnum: {
        "1": {
          text: (
            <Tag color={"orange"}>
              通知
            </Tag>
          ),
          status: 'Default',
        },
        "2": {
          text: (
            <Tag color={"green"}>
              公告
            </Tag>
          ),
          status: 'Processing',
        }
      }
    },
    {
      title: "状态",
      dataIndex: 'status',
      hideInForm: true,
      hideInSearch: true,
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
        }
      }
    },
    {
      title: "创建者",
      dataIndex: 'createBy',
      hideInSearch: true
    },
    {
      title: "创建时间",
      sorter: true,
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true
    },
    {
      title: "操作",
      dataIndex: 'option',
      valueType: 'option',
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
          title="确定要删除通知公告?"
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
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<SYSTEM.SysNotice, SYSTEM.PageParams>
        headerTitle={"通知公告列表"}
        actionRef={actionRef}
        rowKey="noticeId"
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
            新增
          </Button>,
        ]}
        request={sysNoticeList}
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
                title="确定要删除配置项?"
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
        title={"新增通知公告"}
        width="750px"
        visible={createModalVisible}
        modalProps={{destroyOnClose: true}}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as SYSTEM.SysNotice);
          if (success) {
            handleModalVisible(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormGroup>
          <ProFormText
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
          initialValue={"0"}
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
        <ProFormTextArea width="md" name="noticeContent" label={"内容"} />
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
        {currentRow?.noticeId && (
          <ProDescriptions<SYSTEM.SysNotice>
            column={2}
            title={currentRow?.noticeId}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.noticeId,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysNotice>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default NoticeList;
