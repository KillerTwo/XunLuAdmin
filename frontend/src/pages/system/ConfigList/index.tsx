import {DeleteOutlined, EditOutlined, PlusOutlined} from '@ant-design/icons';
import {Button, message, Drawer, Tag, Space, Popconfirm} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {ModalForm, ProFormRadio, ProFormText, ProFormTextArea} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {addSysConfig, removeSysConfig, sysConfigList, updateSysConfig} from "@/services/system/sysConfig";
import {SYSTEM} from "@/services/system/typings";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: SYSTEM.SysConfig) => {
  const hide = message.loading('正在添加');
  try {
    await addSysConfig({ ...fields });
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
  const hide = message.loading('修改');
  try {
    await updateSysConfig({
      ...fields
    });
    hide();

    message.success('修改成功');
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
const handleRemove = async (selectedRows: SYSTEM.SysConfig[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeSysConfig([...selectedRows.map((row) => row.configId)])
    hide();
    message.success('删除成功');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请稍后重试!');
    return false;
  }
};

const ConfigList: React.FC = () => {
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
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysConfig>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysConfig[]>([]);


  const columns: ProColumns<SYSTEM.SysConfig>[] = [
    {
      title: "参数主键",
      dataIndex: 'configId',
      hideInSearch: true
    },
    {
      title: "参数名称",
      dataIndex: 'configName',
    },
    {
      title: "参数键名",
      dataIndex: 'configKey',
    },
    {
      title: "参数键值",
      dataIndex: 'configValue',
      hideInSearch: true
    },
    {
      title: "系统内置",
      dataIndex: 'configType',
      hideInForm: true,
      valueEnum: {
        "Y": {
          text: (
            <Tag color={"green"}>
              是
            </Tag>
          ),
          status: 'Default',
        },
        "N": {
          text: (
            <Tag color={"red"}>
              否
            </Tag>
          ),
          status: 'Processing',
        }
      },
    },
    {
      title: "备注",
      dataIndex: 'remark',
      valueType: 'textarea',
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
          title="确定要删除配置?"
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
      <ProTable<SYSTEM.SysConfig, SYSTEM.PageParams>
        headerTitle={"参数配置列表"}
        actionRef={actionRef}
        rowKey="configId"
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
        request={sysConfigList}
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
        title={"新增"}
        width="600px"
        modalProps={{destroyOnClose: true}}
        visible={createModalVisible}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as SYSTEM.SysConfig);
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
              message: "参数名称不能为空",
            },
          ]}
          width="md"
          name="configName"
          label={"参数名称"}
        />
        <ProFormText
          rules={[
            {
              required: true,
              message: "参数键名不能为空",
            },
          ]}
          width="md"
          name="configKey"
          label={"参数键名"}
        />
        <ProFormText
          rules={[
            {
              required: true,
              message: "参数键值不能为空",
            },
          ]}
          width="md"
          name="configValue"
          label={"参数键值"}
        />
        <ProFormRadio.Group
          width={"md"}
          initialValue={"Y"}
          name="configType"
          label="系统内置"
          options={[
            {
              label: '是',
              value: 'Y',
            },
            {
              label: '否',
              value: 'N',
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
        {currentRow?.configName && (
          <ProDescriptions<SYSTEM.SysConfig>
            column={2}
            title={currentRow?.configName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.configName,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysConfig>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default ConfigList;
