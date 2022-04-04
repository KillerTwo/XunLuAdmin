import {message, Table, Drawer, Tag, Space, Popconfirm, TreeSelect,Form,Input, Button,Select,Card} from 'antd';
import React, {useState, useEffect} from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import {ModalForm, ProForm, ProFormText, ProFormDigit, ProFormRadio, ProFormTreeSelect} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {SYSTEM} from "@/services/system/typings";
import {addSysDept, removeSysDept, sysDeptList, sysDeptSelectList, updateSysDept} from "@/services/system/sysDept";
import {handleTree} from "@/utils/sysMenu";
import {ColumnsType} from "antd/es/table";
import {EditOutlined,PlusOutlined,DeleteOutlined} from "@ant-design/icons";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: SYSTEM.SysDept) => {
  const hide = message.loading('正在添加');
  try {
    await addSysDept({ ...fields });
    hide();
    message.success('Added successfully');
    return true;
  } catch (error) {
    hide();
    message.error('Adding failed, please try again!');
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
  const hide = message.loading('Configuring');
  try {
    await updateSysDept({
      ...fields
    });
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
const handleRemove = async (selectedRows: SYSTEM.SysDept[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeSysDept(selectedRows.map((row) => row.deptId));
    hide();
    message.success('删除成功');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败, 请稍后重试');
    return false;
  }
};

const DeptList: React.FC = () => {
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

  const [currentRow, setCurrentRow] = useState<SYSTEM.SysDept>();
  const [defaultParent, setDefaultParent] = useState<number|0>(0);
  const [data, setData] = useState<SYSTEM.SysDept[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const handleRequestDept = (params?: SYSTEM.SysDept | {}) => {
    setLoading(true);
    sysDeptList({...params}).then((res: SYSTEM.ResponseResult) => {
      setLoading(false);
      const treeMenu = handleTree(res.data, 'deptId');
      console.log("treeMenu: ", treeMenu);
      setData(treeMenu);
    })
  }

  const columns: ColumnsType<SYSTEM.SysDept> = [
    {
      title: '部门名称',
      dataIndex: 'deptName',
      key: 'deptName',
    },
    {
      title: '排序',
      dataIndex: 'orderNum',
      key: 'orderNum',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (record: string) => {
        const color = record === "0" ? "green" : "red";
        const text = record === "0" ? "正常" : "停用";
        return (
          <Tag color={color}>
            {text}
          </Tag>
        );
      }
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
    },
    {
      title: '操作',
      dataIndex: 'options',
      key: 'options',
      render: (text: any, record: SYSTEM.SysDept) => (
        <Space size="middle">

          <a onClick={() => {
            setCurrentRow(record);
            handleUpdateModalVisible(true);
          }}><EditOutlined />修改</a>
          <a onClick={() => {
            setDefaultParent(record.deptId || 0);
            handleModalVisible(true);
          }}><PlusOutlined />新增</a>
          <Popconfirm
            title="确定要删除菜单?"
            onConfirm={async () => {
              const result = await handleRemove([record]);
              if (result) {
                handleRequestDept({});
              }
            }}
            onCancel={() => {
            }}
            okText="确定"
            cancelText="取消"
          >
            <a><DeleteOutlined />删除</a>
          </Popconfirm>

        </Space>
      ),
    },
  ];



  useEffect(() => {
    /*sysMenuList({}).then((res: SYSTEM.ResponseResult) => {
      const treeMenu = handleTree(res.data, 'menuId');
      console.log("treeMenu: ", treeMenu);
      setData(treeMenu);
    })*/
    handleRequestDept({})
  }, []);

  const [form] = Form.useForm();

  return (
    <PageContainer>
      <>
        <Card bordered={false} style={{ width: "100%", marginBottom: 20 }}>
          <Form form={form} name="horizontal_login" layout="inline" onFinish={(values: any) => {handleRequestDept({...values})}}>
            <Form.Item
              name="deptName"
              label={"部门名称"}
            >
              <Input placeholder="部门名称" />
            </Form.Item>
            <Form.Item
              label={"状态"}
              name="status"
            >
              <Select style={{width: 200}}>
                <Select.Option value="0" key={0}>正常</Select.Option>
                <Select.Option value="1" key={1}>停用</Select.Option>
              </Select>
            </Form.Item>
            <Form.Item shouldUpdate>
              <Button
                type="primary"
                htmlType="submit"
              >
                查询
              </Button>
            </Form.Item>
            <Form.Item shouldUpdate>
              <Button
                type="primary"
                htmlType="button"
                onClick={() => {
                  form.resetFields();
                  handleRequestDept({});
                }}
              >
                重置
              </Button>
            </Form.Item>
          </Form>
        </Card>
        <Table
          rowKey={"deptId"}
          columns={columns}
          dataSource={data}
          pagination={false}
          loading={loading}
        />
        <ModalForm
          modalProps={{destroyOnClose: true}}
          title={'添加菜单'}
          width="70%"
          visible={createModalVisible}
          onVisibleChange={handleModalVisible}

          onFinish={async (value) => {
            const success = await handleAdd(value as SYSTEM.SysMenu);
            if (success) {
              handleModalVisible(false);
              handleRequestDept({});
            }
          }}
        >
          <ProForm.Group>
            <ProFormTreeSelect
              initialValue={defaultParent}
              width={"md"}
              label="上级部门"
              request={async () => {
                const resData = await sysDeptSelectList();
                return resData.data;
              }}
              fieldProps={{
                fieldNames: {
                  label: 'label',
                  value: 'id',
                  children: 'children'
                },
                showCheckedStrategy: TreeSelect.SHOW_PARENT
              }}
              rules={[
                {
                  required: true,
                  message: "上级部门不能为空"
                }
              ]}
              name={"parentId"}
            />
          </ProForm.Group>
          <ProForm.Group>
            <ProFormText
              rules={[
                {
                  required: true,
                  message: "部门名称必填",
                },
              ]}
              width="md"
              name="deptName"
              label={"部门名称"}
            />
            <ProFormDigit
              rules={[
                {
                  required: true,
                  message: "显示排序必填",
                },
              ]}
              width="md"
              name="orderNum"
              label={"显示排序"}
              min={0}
              max={999}
            />
          </ProForm.Group>
          <ProForm.Group>
            <ProFormText
              width="md"
              name="leader"
              label={"负责人"}
            />
            <ProFormText
              width="md"
              name="phone"
              label={"联系电话"}
            />
          </ProForm.Group>
          <ProForm.Group>
            <ProFormText
              width="md"
              name="email"
              label={"邮箱"}
            />
            <ProFormRadio.Group
              initialValue={"0"}
              width={"md"}
              name="status"
              label="部门状态"
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
        </ModalForm>
        <UpdateForm
          onSubmit={async (value) => {
            const success = await handleUpdate({...currentRow, ...value});
            if (success) {
              handleUpdateModalVisible(false);
              setCurrentRow(undefined);
              handleRequestDept({});
            }
          }}
          onCancel={(value: boolean | undefined) => {
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
          {currentRow?.deptName && (
            <ProDescriptions<SYSTEM.SysDept>
              column={2}
              title={currentRow?.deptName}
              request={async () => ({
                data: currentRow || {},
              })}
              params={{
                id: currentRow?.deptName,
              }}
              columns={columns as ProDescriptionsItemProps<SYSTEM.SysDept>[]}
            />
          )}
        </Drawer>
      </>
    </PageContainer>
  );
};

export default DeptList;
