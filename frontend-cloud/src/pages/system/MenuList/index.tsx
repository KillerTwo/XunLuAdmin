import {message, Table, Drawer, Tag, Space, Popconfirm, TreeSelect,Form,Input, Button,Select,Card} from 'antd';
import React, {useState, useEffect} from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import {
  ModalForm,
  ProForm,
  ProFormText,
  ProFormDigit,
  ProFormRadio,
  ProFormTreeSelect,
  ProFormSelect
} from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {SYSTEM} from "@/services/system/typings";
import {addSysMenu, removeSysMenu, sysMenuList, sysMenuSelectList, updateSysMenu} from "@/services/system/sysMenu";
import {handleTree} from "@/utils/sysMenu";
import {ColumnsType} from "antd/es/table";
import {iconSelect} from "@/utils/routes";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: SYSTEM.SysMenu) => {
  const hide = message.loading('正在添加');
  try {
    await addSysMenu({ ...fields });
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
    await updateSysMenu({
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
const handleRemove = async (selectedRows: SYSTEM.SysMenu[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeSysMenu(selectedRows.map((row) => row.menuId));
    hide();
    message.success('删除成功');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败, 请稍后重试');
    return false;
  }
};

const MenuList: React.FC = () => {
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

  const [currentRow, setCurrentRow] = useState<SYSTEM.SysMenu>();
  const [defaultParent, setDefaultParent] = useState<number|0>(0);
  const [data, setData] = useState<SYSTEM.SysMenu[]>([]);
  const [menuType, setMenuType] = useState<string>("M");
  const [loading, setLoading] = useState<boolean>(false);

  const handleRequestMenu = (params?: SYSTEM.SysMenu | {}) => {
    // const menu: SYSTEM.SysMenu = { menuId: 0, menuName: '主类目', children: [] };
    setLoading(true);
    sysMenuList({...params}).then((res: SYSTEM.ResponseResult) => {
      setLoading(false);
      const treeMenu = handleTree(res.data, 'menuId');
      console.log("treeMenu: ", treeMenu);
      // menu.children = treeMenu;
      setData(treeMenu);
    })
  }

  const columns: ColumnsType<SYSTEM.SysMenu> = [
    {
      title: '菜单名称',
      dataIndex: 'menuName',
      key: 'menuName',
    },
    {
      title: '图标',
      dataIndex: 'icon',
      key: 'icon',
    },
    {
      title: '排序',
      dataIndex: 'orderNum',
      key: 'orderNum',
    },
    {
      title: '权限标识',
      dataIndex: 'perms',
      key: 'perms',
    },
    {
      title: '组件路径',
      dataIndex: 'component',
      key: 'component',
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
      render: (text: any, record: SYSTEM.SysMenu) => (
        <Space size="middle">
          <a onClick={() => {
            setCurrentRow(record);
            handleUpdateModalVisible(true);
          }}>修改</a>
          <a onClick={() => {
            setDefaultParent(record.menuId || 0);
            setMenuType("M");
            handleModalVisible(true);
          }}>新增</a>
          <Popconfirm
            title="确定要删除菜单?"
            onConfirm={async () => {
              const result = await handleRemove([record]);
              if (result) {
                handleRequestMenu({});
              }
            }}
            onCancel={() => {
            }}
            okText="确定"
            cancelText="取消"
          >
            <a>删除</a>
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
    handleRequestMenu({})
  }, []);

  const [form] = Form.useForm();

  return (
    <PageContainer>
      <>
      <Card bordered={false} style={{ width: "100%", marginBottom: 20 }}>
        <Form form={form} name="horizontal_login" layout="inline" onFinish={(values: any) => {handleRequestMenu({...values})}}>
          <Form.Item
            name="menuName"
            label={"菜单名称"}
          >
            <Input placeholder="菜单名称" />
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
                handleRequestMenu({});
              }}
            >
              重置
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Button
        type="primary"
        htmlType="button"
        onClick={() => {
          setDefaultParent(0);
          setMenuType("M");
          handleModalVisible(true);
        }}
      >
        新增
      </Button>
      <Table
        rowKey={"menuId"}
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
        onValuesChange={(changedValues) => {
          if (changedValues["menuType"]) {
            setMenuType(changedValues["menuType"]);
          }
          // setMenuType(changedValues);
        }}
        onFinish={async (value) => {
          const success = await handleAdd(value as SYSTEM.SysMenu);
          if (success) {
            handleModalVisible(false);
            handleRequestMenu({});
          }
        }}
      >
        <ProForm.Group>
          <ProFormTreeSelect
            initialValue={defaultParent}
            width={"md"}
            label="上级菜单"
            request={async () => {
              const resData = await sysMenuSelectList();
              const menu: {id: number, label: string, children: []} = { id: 0, label: '主类目', children: [] };
              menu.children = resData.data;
              console.log("resData.data: ", resData.data);
              // return resData.data;
              return [menu] || [];
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
                message: "上级菜单不能为空"
              }
            ]}
            name={"parentId"}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormRadio.Group
            initialValue={menuType}
            width={"md"}
            name="menuType"
            label="菜单类型"
            options={[
              {
                label: '目录',
                value: 'M',
              },
              {
                label: '菜单',
                value: 'C',
              },
              {
                label: '按钮',
                value: 'F',
              },
            ]}
          />
        </ProForm.Group>
        <ProForm.Group>
          {/*<ProFormText
            width="md"
            name="icon"
            label={"菜单图标"}
          />*/}
          <ProFormSelect
            width="md"
            name="icon"
            label={"菜单图标"}
            valueEnum={() => {
              return iconSelect();
            }}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormText
            rules={[
              {
                required: true,
                message: "菜单名称必填",
              },
            ]}
            width="md"
            name="menuName"
            label={"菜单名称"}
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
            min={1}
            max={999}
          />
        </ProForm.Group>
        {
          (menuType === "C" || menuType === "M") && (
            <>
              <ProForm.Group>
                <ProFormRadio.Group
                  initialValue={"1"}
                  width={"md"}
                  rules={[
                    {
                      required: true,
                      message: "是否是外链必填",
                    },
                  ]}
                  name="isFrame"
                  label="是否是外链"
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
                <ProFormText
                  rules={[
                    {
                      required: true,
                      message: "路由地址必填",
                    },
                  ]}
                  width="md"
                  name="path"
                  label={"路由地址"}
                />
              </ProForm.Group>
              {
                menuType === "C" && (
                  <>
                    <ProForm.Group>
                      <ProFormText
                        width="md"
                        name="component"
                        label={"组件路径"}
                      />
                      <ProFormText
                        width="md"
                        name="perms"
                        label={"权限字符串"}
                      />
                    </ProForm.Group>
                    <ProForm.Group>
                      <ProFormText
                        width="md"
                        name="query"
                        label={"路由参数"}
                      />
                      <ProFormRadio.Group
                        initialValue={"0"}
                        width={"md"}
                        name="isCache"
                        label="是否缓存"
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
                    </ProForm.Group>
                  </>

                )
              }
              <ProForm.Group>
                <ProFormRadio.Group
                  initialValue={"0"}
                  width={"md"}
                  name="visible"
                  label="显示状态"
                  options={[
                    {
                      label: '显示',
                      value: '0',
                    },
                    {
                      label: '隐藏',
                      value: '1',
                    }
                  ]}
                />
                <ProFormRadio.Group
                  initialValue={"0"}
                  width={"md"}
                  name="status"
                  label="菜单状态"
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
            </>
          )
        }

      </ModalForm>
      <UpdateForm
        onSubmit={async (value) => {
          const success = await handleUpdate({...currentRow, ...value});
          if (success) {
            handleUpdateModalVisible(false);
            setCurrentRow(undefined);
            handleRequestMenu({});
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
        {currentRow?.menuName && (
          <ProDescriptions<SYSTEM.SysMenu>
            column={2}
            title={currentRow?.menuName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.menuName,
            }}
            columns={columns as ProDescriptionsItemProps<SYSTEM.SysMenu>[]}
          />
        )}
      </Drawer>
      </>
    </PageContainer>
  );
};

export default MenuList;
