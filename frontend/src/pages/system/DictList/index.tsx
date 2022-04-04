import {DeleteOutlined, EditOutlined, PlusOutlined} from '@ant-design/icons';
import {Button, message, Drawer, Tag, Popconfirm, Space} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {ModalForm, ProFormRadio, ProFormText, ProFormTextArea} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {addSysDictType, removeSysDictType, sysDictTypeList, updateSysDictType} from "@/services/system/sysDict";
import {SYSTEM} from "@/services/system/typings";
import {history} from "umi";
import {useModel} from "@@/plugin-model/useModel";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: SYSTEM.SysDictType) => {
  const hide = message.loading('正在添加');
  try {
    await addSysDictType({ ...fields });
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
    await updateSysDictType({...fields});
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
const handleRemove = async (selectedRows: SYSTEM.SysDictType[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeSysDictType([...selectedRows.map((row) => row.dictId),
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

const DictList: React.FC = () => {
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
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysDictType>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysDictType[]>([]);

  const {setDictType} = useModel("dict", model => {return {setDictType: model.setDictType}});
  const columns: ProColumns<SYSTEM.SysDictType>[] = [
    {
      title: "字典编号",
      dataIndex: 'dictId',
      hideInSearch: true,
      key: "dictId",
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
      title: "字典名称",
      dataIndex: 'dictName',
      key: "dictName",
    },
    {
      title: "字典类型",
      dataIndex: 'dictType',
      key: "dictType",
      render: (dom, entity) => {
        return (
          <a
            onClick={() => {
              setCurrentRow(entity);
              setShowDetail(true);
              setDictType(entity.dictType || "")
              history.push(`/system/dictData`)
            }}
          >
            {dom}
          </a>
        );
      },
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
      title: "备注",
      dataIndex: 'remark',
      hideInSearch: true,
      key: "remark",
    },
    {
      title: "创建时间",
      sorter: true,
      dataIndex: 'createTime',
      hideInSearch: true,
      valueType: 'dateTime',
      key: "createTime",
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
          title="确定要删除字典类型?"
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
      <ProTable<SYSTEM.SysDictType, SYSTEM.PageParams>
        headerTitle="字典类型列表"
        actionRef={actionRef}
        rowKey="dictId"
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
        request={sysDictTypeList}
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
                title="确定要删除字典配置?"
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
        title="新增字典类型"
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
              message: "字典名称不能为空",
            },
          ]}
          width="md"
          name="dictName"
          label="字典名称"
        />
        <ProFormText
          rules={[
            {
              required: true,
              message: "字典类型不能为空",
            },
          ]}
          width="md"
          name="dictType"
          label="字典类型"
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
        <ProFormTextArea width="md" name="remark" label="备注" />
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
        {currentRow?.dictName && (
          <ProDescriptions<SYSTEM.SysDictType>
            column={2}
            title={currentRow?.dictName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.dictId,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysDictType>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default DictList;
