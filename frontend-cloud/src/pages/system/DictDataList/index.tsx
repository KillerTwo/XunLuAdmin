import {DeleteOutlined, EditOutlined, PlusOutlined} from '@ant-design/icons';
import {Button, message, Drawer, Tag, Popconfirm, Space} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {ModalForm, ProFormDigit, ProFormRadio, ProFormText, ProFormTextArea} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {SYSTEM} from "@/services/system/typings";
import {addSysDictData, removeSysDictData, sysDictDataList, updateSysDictData} from "@/services/system/sysDictData";
import {useModel} from "@@/plugin-model/useModel";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: SYSTEM.SysDictData) => {
  const hide = message.loading('正在添加');
  try {
    await addSysDictData({ ...fields });
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
    await updateSysDictData({...fields});
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
const handleRemove = async (selectedRows: SYSTEM.SysDictData[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeSysDictData([...selectedRows.map((row) => row.dictCode),
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

const DictDataList: React.FC = () => {
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
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysDictData>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysDictData[]>([]);

  const {dictType} = useModel("dict", model => {return {dictType: model.dictType}});

  const columns: ProColumns<SYSTEM.SysDictData>[] = [
    {
      title: "字典编码",
      dataIndex: 'dictCode',
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
      title: "字典标签",
      dataIndex: 'dictLabel',
    },
    {
      title: "字典键值",
      dataIndex: 'dictValue',
      hideInSearch: true
    },
    {
      title: "字典排序",
      dataIndex: 'dictSort',
      hideInSearch: true
    },
    {
      title: "状态",
      dataIndex: 'status',
      hideInForm: true,
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
      hideInSearch: true
    },
    {
      title: "创建时间",
      sorter: true,
      dataIndex: 'createTime',
      hideInSearch: true,
      valueType: 'dateTime',
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
          title="确定要删除字典数据?"
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
      <ProTable<SYSTEM.SysDictData, SYSTEM.PageParams>
        headerTitle="字典数据列表"
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
        request={(params) => {
          return sysDictDataList({...params, dictType: dictType});}}
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
        title="新增字典数据"
        width="600px"
        visible={createModalVisible}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as SYSTEM.SysDictData);
          if (success) {
            handleModalVisible(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormText
          width="md"
          name="dictType"
          label="字典类型"
          disabled={true}
        />
        <ProFormText
          rules={[
            {
              required: true,
              message: "数据标签不能为空",
            },
          ]}
          width="md"
          name="dictLabel"
          label="数据标签"
        />
        <ProFormText
          rules={[
            {
              required: true,
              message: "数据键值不能为空",
            },
          ]}
          width="md"
          name="dictValue"
          label="数据键值"
        />
        <ProFormText
          width="md"
          name="cssClass"
          label="样式属性"
        />
        <ProFormDigit
          rules={[
            {
              required: true,
              message: "字典顺序不能为空",
            },
          ]}
          width="md"
          name="dictSort"
          label={"字典顺序"}
          min={0}
          max={100}
        />
        <ProFormText
          width="md"
          name="listClass"
          label="回显样式"
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
        {currentRow?.dictLabel && (
          <ProDescriptions<SYSTEM.SysDictData>
            column={2}
            title={currentRow?.dictLabel}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.dictCode,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysDictData>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default DictDataList;
