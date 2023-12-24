import { PlusOutlined } from '@ant-design/icons';
import {Button, message, Drawer, Tag, Space, TreeSelect} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {
  ModalForm,
  ProFormText,
  ProFormTextArea,
  ProForm,
  ProFormDigit,
  ProFormRadio,
  ProFormTreeSelect
} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {SYSTEM} from "@/services/system/typings";
import {addRole, removeRole, sysRoleList, updateRole} from "@/services/system/sysRole";
import {sysMenuSelectList, sysMenuSelectRoleList} from "@/services/system/sysMenu";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: SYSTEM.SysRole) => {
  const hide = message.loading('正在添加');
  try {
    await addRole({ ...fields });
    hide();
    message.success('添加成功');
    return true;
  } catch (error) {
    hide();
    message.error('添加失败, 请稍后重试!');
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
  const hide = message.loading('修改角色');
  try {
    await updateRole(fields);
    hide();
    message.success('更新成功');
    return true;
  } catch (error) {
    hide();
    message.error('更新失败, 请稍后重试!');
    return false;
  }
};

/**
 *  Delete node
 * @zh-CN 删除节点
 *
 * @param selectedRows
 */
const handleRemove = async (selectedRows: SYSTEM.SysRole[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeRole(
      selectedRows.map((row) => row.roleId),
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

const RoleList: React.FC = () => {
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
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysRole>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysRole[]>([]);

  const columns: ProColumns<SYSTEM.SysRole>[] = [
    {
      title: "角色名称",
      dataIndex: "roleName",
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
      title: "角色标识",
      dataIndex: 'roleKey',
    },
    {
      title: "状态",
      dataIndex: 'status',
      hideInForm: true,
      hideInSearch: true,
      valueEnum: {
        0: {
          text: (
            <Tag color={'green'}>
              有效
            </Tag>
          ),
          status: 'Default',
        },
        1: {
          text: (
            <Tag color={'red'}>
              无效
            </Tag>
          ),
          status: 'Processing',
        },
      },
    },
    {
      title: "创建时间",
      sorter: true,
      hideInSearch: true,
      dataIndex: 'createTime',
      valueType: 'dateTime'
    },
    {
      title: "操作",
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => {
        if (record.roleKey === "admin") {
          return [];
        }
        return [
          <a
            key="modify"
            onClick={async () => {
              const roleMenus = await sysMenuSelectRoleList(record.roleId);
              if (roleMenus.code === 200) {
                const checkedKeys = roleMenus.data.checkedKeys;
                handleUpdateModalVisible(true);
                setCurrentRow({...record, menuIds: checkedKeys});
              } else {
                message.error('查询菜单权限失败!');
              }
            }}
          >
            修改
          </a>,
          <a
            key="remove"
            onClick={async () => {
              const success = await handleRemove([record]);
              if (success) {
                if (actionRef.current) {
                  actionRef.current.reload();
                }
              }
            }}
          >
            删除
          </a>
        ]
      }
    }];
  return (
    <PageContainer>
      <ProTable<SYSTEM.SysRole, SYSTEM.PageParams & SYSTEM.SysRole>
        headerTitle={"角色列表"}
        actionRef={actionRef}
        rowKey="roleId"
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
        request={sysRoleList}
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
              <Button
                onClick={async () => {
                  await handleRemove(selectedRowsState);
                  setSelectedRows([]);
                  actionRef.current?.reloadAndRest?.();
                }}
              >
                批量删除
              </Button>
            </Space>
          );
        }}
      />
      <ModalForm
        modalProps={{destroyOnClose: true}}
        title={"新增角色"}
        width="60%"
        visible={createModalVisible}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as SYSTEM.SysRole);
          if (success) {
            handleModalVisible(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProForm.Group>
          <ProFormText
            rules={[
              {
                required: true,
                message: "角色名称不能为空",
              },
            ]}
            label={"角色名称"}
            width="md"
            name="roleName"
          />
          <ProFormText
            rules={[
              {
                required: true,
                message: "角色标识不能为空",
              },
            ]}
            label={"角色标识"}
            width="md"
            name="roleKey"
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormDigit width="md" name="roleSort" label="角色顺序" initialValue={1} />
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
        </ProForm.Group>
        <ProForm.Group>
          <ProFormTreeSelect
            width={"md"}
            label="菜单权限"
            request={async () => {
              const resData = await sysMenuSelectList();
              return resData.data;
            }}
            fieldProps={{
              fieldNames: {
                label: 'label',
                value: 'id',
                children: 'children'
              },
              showCheckedStrategy: TreeSelect.SHOW_ALL,
              treeCheckable: true
            }}
            name={"menuIds"}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormTextArea width="md" label={"备注"} name="remark" />
        </ProForm.Group>
      </ModalForm>
      <UpdateForm
        onSubmit={async (value) => {
          const success = await handleUpdate({...value, roleId: currentRow?.roleId});
          if (success) {
            handleUpdateModalVisible(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={(value) => {
          if(!value) {
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
        {currentRow?.roleName && (
          <ProDescriptions<SYSTEM.SysRole>
            column={2}
            title={currentRow?.roleName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.roleId,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysRole>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};

export default RoleList;
