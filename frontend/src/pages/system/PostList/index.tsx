import {DeleteOutlined, EditOutlined, PlusOutlined} from '@ant-design/icons';
import {Button, message, Drawer, Tag, Space,Popconfirm} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {ModalForm, ProFormDigit, ProFormRadio, ProFormText, ProFormTextArea} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {addSysPost, deleteSysPost, sysPostList, updateSysPost} from "@/services/system/sysPost";
import {SYSTEM} from "@/services/system/typings";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: SYSTEM.SysPost) => {
  const hide = message.loading('正在添加');
  try {
    await addSysPost({ ...fields });
    hide();
    message.success('添加成功');
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
  const hide = message.loading('更新中');
  try {
    await updateSysPost({...fields});
    hide();

    message.success('更新成功');
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
const handleRemove = async (selectedRows: SYSTEM.SysPost[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await deleteSysPost(
      [...selectedRows.map((row) => row.postId)]
    );
    hide();
    message.success('删除成功！');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请稍后重试！');
    return false;
  }
};

const PostList: React.FC = () => {
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
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysPost>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysPost[]>([]);


  const columns: ProColumns<SYSTEM.SysPost>[] = [
    {
      title: "岗位编号",
      dataIndex: 'postId',
      hideInSearch: true
    },
    {
      title: "岗位编码",
      dataIndex: 'postCode',
    },
    {
      title: "岗位名称",
      dataIndex: 'postName'
    },
    {
      title: "岗位排序",
      dataIndex: 'postSort',
      hideInSearch: true
    },
    {
      title: "状态",
      dataIndex: 'status',
      hideInForm: true,
      valueEnum: {
        0: {
          text: (
            <Tag color={"green"}>
              正常
            </Tag>
          ),
          status: 'Default',
        },
        1: {
          text: (
            <Tag color={"red"}>
              停用
            </Tag>
          ),
          status: 'Processing',
        }
      },
    },
    {
      title: "创建时间",
      sorter: true,
      dataIndex: 'createTime',
      hideInSearch: true,
      valueType: 'dateTime'
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
          title="确定要删除岗位?"
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
        </Popconfirm>
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<SYSTEM.SysPost, SYSTEM.PageParams>
        headerTitle={"岗位列表"}
        actionRef={actionRef}
        rowKey="postId"
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
            <PlusOutlined />新增
          </Button>,
        ]}
        request={sysPostList}
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
                title="确定要删除岗位?"
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
        title={"新增岗位"}
        width="400px"
        visible={createModalVisible}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as SYSTEM.SysPost);
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
              message: "岗位名称不能为空",
            },
          ]}
          width="md"
          name="postName"
          label={"岗位编码"}
        />
        <ProFormText
          rules={[
            {
              required: true,
              message: "岗位编码不能为空",
            },
          ]}
          width="md"
          name="postCode"
          label={"岗位编码"}
        />
        <ProFormDigit
          rules={[
            {
              required: true,
              message: "岗位顺序不能为空",
            },
          ]}
          width="md"
          name="postSort"
          label={"岗位顺序"}
          min={0}
          max={100}
        />
        <ProFormRadio.Group
          width={"md"}
          initialValue={0}
          name="status"
          label="岗位状态"
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
        <ProFormTextArea width="md" name="remark" label={"备注"} />
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
        {currentRow?.postName && (
          <ProDescriptions<SYSTEM.SysPost>
            column={2}
            title={currentRow?.postName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.postName,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysPost>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default PostList;
