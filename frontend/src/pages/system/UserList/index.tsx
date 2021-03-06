import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import { Button, message, Drawer, Tag, TreeSelect, Space, Popconfirm } from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {
  ModalForm,
  ProFormText,
  ProFormTextArea,
  ProFormSelect,
  ProForm,
  ProFormTreeSelect,
  ProFormInstance,
} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {
  addUser,
  getSysUserInfo,
  removeUser,
  sysUserList,
  updateUser,
} from '@/services/system/sysUser';
import { SYSTEM } from '@/services/system/typings';
import { sysRoleSelectList } from '@/services/system/sysRole';
import { sysPostSelectList } from '@/services/system/sysPost';
import { sysDeptSelectList } from '@/services/system/sysDept';
/**
 * @en-US Add User
 * @zh-CN 添加用户
 * @param fields
 */

const handleAdd = async (fields: SYSTEM.SysUser) => {
  const hide = message.loading('正在添加');

  try {
    console.log('用户信息： ', fields);
    await addUser({ ...fields });
    hide();
    message.success('添加成功！');
    return true;
  } catch (error) {
    hide();
    message.error('添加失败，请重试!');
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
    await updateUser(fields);
    hide();
    message.success('更新成功！');
    return true;
  } catch (error) {
    hide();
    message.error('更新失败，请重试！');
    return false;
  }
};
/**
 *  Delete node
 * @zh-CN 删除节点
 *
 * @param selectedRows
 */

const handleRemove = async (selectedRows: SYSTEM.SysUser[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;

  try {
    const userIds = selectedRows.map((row) => row.userId);
    if (!userIds) return true;
    await removeUser(userIds);
    hide();
    message.success('删除成功！');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请重试');
    return false;
  }
};

const UserList: React.FC = () => {
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
  const [currentRow, setCurrentRow] = useState<SYSTEM.SysUser>();
  const [selectedRowsState, setSelectedRows] = useState<SYSTEM.SysUser[]>([]);
  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */

  const columns: ProColumns<SYSTEM.SysUser>[] = [
    {
      title: '用户名称',
      dataIndex: 'userName',
      key: 'userName',
      tip: 'The rule name is the unique key',
      render: (dom, entity) => {
        return (
          <a
            key={entity.userId}
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
      title: '用户昵称',
      dataIndex: 'nickName',
      valueType: 'text',
      key: 'nickName',
    },
    {
      title: '部门',
      dataIndex: 'dept',
      // hideInSearch: true,
      valueType: 'treeSelect',
      key: 'treeSelect',
      request: async () => {
        const resData = await sysDeptSelectList();
        return resData.data;
      },
      fieldProps: {
        fieldNames: {
          label: 'label',
          value: 'id',
          children: 'children',
        },
        showCheckedStrategy: TreeSelect.SHOW_PARENT,
      },
      renderText: (val: SYSTEM.SysDept) => `${val.deptName}`,
    },
    {
      title: '手机号码',
      dataIndex: 'phonenumber',
      hideInSearch: true,
      valueType: 'text',
      key: 'phonenumber',
    },
    {
      title: '状态',
      dataIndex: 'status',
      hideInForm: true,
      hideInSearch: true,
      key: 'status',
      valueEnum: {
        0: {
          text: <Tag color={'green'}>有效</Tag>,
          status: 'Default',
        },
        1: {
          text: <Tag color={'red'}>无效</Tag>,
          status: 'Processing',
        },
      },
    },
    {
      title: '创建时间',
      sorter: true,
      dataIndex: 'createTime',
      hideInSearch: true,
      valueType: 'dateTime',
      key: 'createTime',
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      key: 'option',
      render: (_, record) => [
        <a
          key={record.userId}
          onClick={async () => {
            const resData = await getSysUserInfo(record.userId);
            setCurrentRow({
              ...record,
              roleIds: resData.data?.roleIds,
              postIds: resData.data?.postIds,
            });
            handleUpdateModalVisible(true);
          }}
        >
          <EditOutlined />
          修改
        </a>,
        <Popconfirm
          key={record.nickName}
          title="确定要删除用户?"
          onConfirm={async () => {
            await handleRemove([record]);
            actionRef.current?.reloadAndRest?.();
          }}
          onCancel={() => {}}
          okText="确定"
          cancelText="取消"
        >
          <a key={record.phonenumber}>
            <DeleteOutlined />
            删除
          </a>
        </Popconfirm>,
      ],
    },
  ];
  const tableRef = useRef<ProFormInstance>(); // const [deptTreeData, setDeptTreeData] = useState<SYSTEM.DeptTree[]|[]>([]);

  /*useEffect(() => {
    sysDeptSelectList().then(res => {
      setDeptTreeData(res.data);
    });
  }, [])*/

  /*const onSelect = (value: any) => {
    if (tableRef.current) {
      tableRef.current.setFieldsValue({
        dept: value
      });
      tableRef.current.submit();
    }
  }*/

  return (
    <PageContainer>
      {/*<DeptTree data={deptTreeData} onSelect={onSelect} />*/}
      <ProTable<
        SYSTEM.SysUser,
        SYSTEM.PageParams & {
          dept: number;
          userName: string;
          nickName: string;
        }
      >
        headerTitle={'用户信息'}
        actionRef={actionRef}
        pagination={{
          pageSize: 10,
          showSizeChanger: true,
        }}
        rowKey="userId"
        search={{
          labelWidth: 120,
        }}
        formRef={tableRef}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalVisible(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={(params, sorter, filter) => {
          console.log(params, sorter, filter);
          const { dept: deptId = null, ...rest } = params;
          return sysUserList({
            deptId,
            ...rest,
          });
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
              <a
                style={{
                  marginLeft: 8,
                }}
                onClick={onCleanSelected}
              >
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
        title={'新增用户'}
        {...{
          labelCol: {
            span: 6,
          },
          wrapperCol: {
            span: 18,
          },
        }}
        width="60%"
        layout={'horizontal'}
        visible={createModalVisible}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as SYSTEM.SysUser);

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
                message: '用户名不能为空',
              },
            ]}
            width="md"
            name="userName"
            label={'用户名称'}
          />
          <ProFormText
            rules={[
              {
                required: true,
                message: '用户昵称不能为空',
              },
            ]}
            width="md"
            name="nickName"
            label={'用户昵称'}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormText
            rules={[
              {
                required: true,
                message: '手机号不能为空',
              },
            ]}
            width="md"
            name="phonenumber"
            label={'手机号码'}
          />
          <ProFormText
            rules={[
              {
                required: true,
                message: '邮箱不能为空',
              },
              {
                type: 'email',
                message: '邮箱格式不合法!',
              },
            ]}
            width="md"
            name="email"
            label={'用户邮箱'}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormTreeSelect
            width={'md'}
            label="所属部门"
            request={async () => {
              const resData = await sysDeptSelectList();
              return resData.data;
            }}
            fieldProps={{
              fieldNames: {
                label: 'label',
                value: 'id',
                children: 'children',
              },
              showCheckedStrategy: TreeSelect.SHOW_PARENT,
            }}
            rules={[
              {
                required: true,
                message: '所属部门不能为空',
              },
            ]}
            name={'deptId'}
          />
          <ProFormSelect
            request={async () => {
              const resData = await sysPostSelectList();
              return resData.data.map((ele: { postId: number; postName: string }) => {
                return {
                  value: ele.postId,
                  label: ele.postName,
                };
              });
            }}
            mode={'multiple'}
            width="md"
            name="postIds"
            label="所在岗位"
            rules={[
              {
                required: true,
                message: '所属岗位不能为空',
              },
            ]}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormSelect
            request={async () => {
              const resData = await sysRoleSelectList();
              return resData.data.map((ele: { roleId: any; roleName: any }) => {
                return {
                  value: ele.roleId,
                  label: ele.roleName,
                };
              });
            }}
            width="md"
            name="roleIds"
            label="所属角色"
            rules={[
              {
                required: true,
                message: '所属角色不能为空',
              },
            ]}
          />
          <ProFormSelect
            options={[
              {
                value: '1',
                label: '男',
              },
              {
                value: '0',
                label: '女',
              },
            ]}
            width="md"
            name="sex"
            label="用户性别"
            rules={[
              {
                required: true,
                message: '用户性别不能为空',
              },
            ]}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormText.Password
            rules={[
              {
                required: true,
                message: '用户密码不能为空',
              },
            ]}
            width="md"
            name="password"
            label={'用户密码'}
          />
          <ProFormTextArea width="md" label="用户备注" name="remark" />
        </ProForm.Group>
      </ModalForm>
      <UpdateForm
        onSubmit={async (value) => {
          const success = await handleUpdate({ ...currentRow, ...value });

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
        {currentRow?.userName && (
          <ProDescriptions<SYSTEM.SysUser>
            column={2}
            title={currentRow?.userName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.userName,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysUser>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};

export default UserList;
