import {
  message,
  Table,
  Drawer,
  Space,
  Popconfirm,
  Form,
  Input,
  Button,
  Card,
  Modal,
  Tabs,
} from 'antd';
import React, { useState, useEffect, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';

import { ColumnsType } from 'antd/es/table';

import {
  genCode,
  getGenTable,
  importTable,
  listDbTable,
  listTable,
  previewTable,
  removeTable,
  synchDb,
  updateGenTable,
} from '@/services/tool/genTable';

import EditTable from '@/pages/tool/Generator/components/EditTable';
import {
  ProForm,
  ProFormInstance,
  ProFormText,
  ProFormTextArea,
  ProFormRadio,
} from '@ant-design/pro-form';
import download from '@/utils/download';
// import hljs from 'highlight.js';

import hljs from 'highlight.js';

const { TabPane } = Tabs;

/**
 * @zh-CN 删除
 * @param selectedRows
 */
const handleRemove = async (selectedRows: Tool.GenTable[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeTable(selectedRows.map((row) => row.tableId));
    hide();
    message.success('删除成功');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败, 请稍后重试');
    return false;
  }
};

/** 高亮显示 */
const highlightedCode = (code: string, key: string) => {
  const vmName = key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'));
  let language = vmName.substring(vmName.indexOf('.') + 1, vmName.length);
  if (language === 'd.ts') {
    language = 'ts';
  }
  const result = hljs.highlight(language, code || '', true);
  return result.value || '&nbsp;';
};

const Generator: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [importModalVisible, handleImportModalVisible] = useState<boolean>(false);
  const [previewModalVisible, handlePreviewModalVisible] = useState<boolean>(false);

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const [currentRow, setCurrentRow] = useState<Tool.GenTable>();
  const [data, setData] = useState<Tool.GenTable[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const [selectedRowKeys, setSelectedRowKeys] = useState([]);

  const [selectedImportRowKeys, setSelectedImportRowKeys] = useState([]);

  const [importData, setImportData] = useState([]);
  const [importTotal, setImportTotal] = useState<number>(0);

  const [tableColumn, setTableColumn] = useState<Tool.GenTableColumn[]>([]);
  const [genForm, setGenForm] = useState<any>({});
  const [basicForm, setBasicForm] = useState<any>({});
  const [tableColumns, setTableColumns] = useState<Tool.GenTableColumn[]>([]);
  const [previewData, setPreviewData] = useState({});

  const handleRequestTable = (params?: (Tool.PageParams & Tool.GenTable) | {}) => {
    setLoading(true);
    listTable({ ...params }).then((res: Tool.ResponseResult) => {
      setLoading(false);
      setData(res.data);
    });
  };

  const handleRequestImportData = async (params?: (Tool.PageParams & Tool.GenTable) | {}) => {
    const resData = await listDbTable(params);
    setImportData(resData.data);
    setImportTotal(resData.total || 0);
  };

  const onSelectChange = (rowKeys: any) => {
    console.log('selectedRowKeys changed: ', rowKeys);
    setSelectedRowKeys(rowKeys);
  };

  const columns: ColumnsType<Tool.GenTable> = [
    {
      title: '表名称',
      dataIndex: 'tableName',
      key: 'tableName',
    },
    {
      title: '表描述',
      dataIndex: 'tableComment',
      key: 'tableComment',
    },
    {
      title: '实体',
      dataIndex: 'className',
      key: 'className',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      key: 'updateTime',
    },
    {
      title: '操作',
      dataIndex: 'options',
      key: 'options',
      render: (text: any, record: Tool.GenTable) => (
        <Space size="middle">
          <a
            onClick={async () => {
              console.log('record: ', record);
              setCurrentRow(record);
              const resData = await getGenTable(record.tableId);
              setTableColumn(resData.data.rows);
              handleUpdateModalVisible(true);
            }}
          >
            编辑
          </a>
          <a
            onClick={async () => {
              // handleModalVisible(true);
              const resData = await previewTable(record.tableId);
              setPreviewData(resData.data);
              console.log('resData: ', resData);
              handlePreviewModalVisible(true);
            }}
          >
            预览
          </a>
          <a
            onClick={async () => {
              const resData = await synchDb(record.tableName);
              if (resData.code === 200) {
                message.success('同步成功！');
              } else {
                message.error('同步失败');
              }
            }}
          >
            同步
          </a>
          <a
            onClick={async () => {
              // handleModalVisible(true);
              console.log('record.genType: ', record.genType);
              if (record.genType === '1') {
                const resData = await genCode(record.tableName);
                if (resData.code === 200) {
                  message.success('生成成功，生成到自定义路径：' + record.genPath);
                } else {
                  message.success('生成失败！');
                }
              } else {
                // this.$download.zip("/tool/gen/batchGenCode?tables=" + tableNames, "ruoyi");
                // 下载zip
                // download.zip('/tool/gen/batchGenCode?tables=' + record.tableName, 'generator');
                download.zip(`/tool/gen/download/${record.tableName}`, 'generator');

                // message.success("下载zip");
              }
            }}
          >
            生成代码
          </a>
          <Popconfirm
            title="确定要删除表?"
            onConfirm={async () => {
              const result = await handleRemove([record]);
              if (result) {
                handleRequestTable({});
              }
            }}
            onCancel={() => {}}
            okText="确定"
            cancelText="取消"
          >
            <a>删除</a>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const importColumns: ColumnsType<any> = [
    {
      title: '表名称',
      dataIndex: 'tableName',
      key: 'tableName',
    },
    {
      title: '表描述',
      dataIndex: 'tableComment',
      key: 'tableComment',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      key: 'updateTime',
    },
  ];

  useEffect(() => {
    handleRequestTable({});
  }, []);

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  const importRowSelection = {
    selectedImportRowKeys,
    onChange: (rowKeys: any) => setSelectedImportRowKeys(rowKeys),
  };

  const [form] = Form.useForm();

  const formRef = useRef<
    ProFormInstance<{
      tableName?: string;
      tableComment?: string;
      className?: string;
      functionAuthor?: string;
      remark?: string;
    }>
  >();

  const genFormRef = useRef<
    ProFormInstance<{
      tplCategory?: string;
      packageName?: string;
      moduleName?: string;
      businessName?: string;
      functionName?: string;
      parentMenuId?: string;
      genType?: string;
      genPath?: string;
    }>
  >();

  return (
    <PageContainer>
      <>
        <Card bordered={false} style={{ width: '100%', marginBottom: 20 }}>
          <Form
            form={form}
            name="horizontal_login"
            layout="inline"
            onFinish={(values: any) => {
              handleRequestTable({ ...values });
            }}
          >
            <Form.Item name="tableName" label={'表名称'}>
              <Input placeholder="表名称" />
            </Form.Item>
            <Form.Item name="tableComment" label={'表描述'}>
              <Input placeholder="表描述" />
            </Form.Item>
            <Form.Item shouldUpdate>
              <Button type="primary" htmlType="submit">
                查询
              </Button>
            </Form.Item>
            <Form.Item shouldUpdate>
              <Button
                type="primary"
                htmlType="button"
                onClick={() => {
                  form.resetFields();
                  handleRequestTable({});
                }}
              >
                重置
              </Button>
            </Form.Item>
          </Form>
        </Card>
        <div style={{ marginBottom: 16 }}>
          <Button
            type="primary"
            onClick={async () => {
              await handleRequestImportData({ current: 1, pageSize: 10 });
              // console.log("resData: ", resData);
              handleImportModalVisible(true);
            }}
          >
            导入
          </Button>
          <span style={{ marginLeft: 8 }}></span>
        </div>
        <Table
          rowKey={'tableId'}
          columns={columns}
          dataSource={data}
          pagination={false}
          loading={loading}
          rowSelection={rowSelection}
        />

        <Modal
          title="预览"
          width={'80%'}
          visible={previewModalVisible}
          onCancel={() => handlePreviewModalVisible(false)}
          footer={[]}
        >
          <Tabs defaultActiveKey="1">
            {Object.keys(previewData)
              .filter((ele) => ele.indexOf('.vue') === -1)
              .map((key) => {
                return (
                  <TabPane
                    tab={key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))}
                    key={key}
                  >
                    <pre>
                      <code
                        className="hljs"
                        dangerouslySetInnerHTML={{ __html: highlightedCode(previewData[key], key) }}
                      ></code>
                    </pre>
                  </TabPane>
                );
              })}
          </Tabs>
        </Modal>

        <Modal
          title="导入"
          width={'80%'}
          visible={importModalVisible}
          onOk={async () => {
            const resData = await importTable(selectedImportRowKeys.join(','));
            if (resData.code === 200) {
              message.success('导入成功！');
              handleImportModalVisible(false);
              handleRequestTable({ current: 1, pageSize: 10 });
            } else {
              message.error('导入失败');
            }
          }}
          onCancel={() => handleImportModalVisible(false)}
        >
          <Card bordered={false} style={{ width: '100%', marginBottom: 20 }}>
            <Form
              form={form}
              name="horizontal_login"
              layout="inline"
              onFinish={(values: any) => {
                handleRequestImportData({ ...values, current: 1, pageSize: 10 });
              }}
            >
              <Form.Item name="tableName" label={'表名称'}>
                <Input placeholder="表名称" />
              </Form.Item>

              <Form.Item name="tableComment" label={'表描述'}>
                <Input placeholder="表描述" />
              </Form.Item>

              <Form.Item shouldUpdate>
                <Button type="primary" htmlType="submit">
                  查询
                </Button>
              </Form.Item>
              <Form.Item shouldUpdate>
                <Button
                  type="primary"
                  htmlType="button"
                  onClick={() => {
                    form.resetFields();
                    handleRequestImportData({ current: 1, pageSize: 10 });
                  }}
                >
                  重置
                </Button>
              </Form.Item>
            </Form>
          </Card>
          <Table
            rowKey={'tableName'}
            columns={importColumns}
            dataSource={importData}
            pagination={{
              total: importTotal,
              pageSize: 10,
              onChange: (current: number, pageSize: number) =>
                handleRequestImportData({ current, pageSize }),
            }}
            loading={loading}
            rowSelection={importRowSelection}
          />
        </Modal>

        <Modal
          title="编辑"
          width={'90%'}
          visible={updateModalVisible}
          onOk={async () => {
            const genTable = Object.assign({}, basicForm, genForm);
            genTable.columns = tableColumns;
            genTable.params = {
              treeCode: genTable.treeCode,
              treeName: genTable.treeName,
              treeParentCode: genTable.treeParentCode,
              parentMenuId: genTable.parentMenuId,
            };
            const resData = await updateGenTable({ ...currentRow, ...genTable });
            if (resData.code === 200) {
              message.success('提交成功');
              handleUpdateModalVisible(false);
            } else {
              message.error('提交失败');
            }
            console.log('resData: ', resData);
          }}
          onCancel={() => handleUpdateModalVisible(false)}
        >
          <Tabs
            defaultActiveKey="1"
            onChange={(key) => {
              console.log(key);
            }}
          >
            <TabPane tab="基本信息" key="1">
              <ProForm<{
                tableName?: string;
                tableComment?: string;
                className?: string;
                functionAuthor?: string;
                remark?: string;
              }>
                onFinish={async (values) => {
                  /*await waitTime(2000);
                  console.log(values);
                  const val1 = await formRef.current?.validateFields();
                  console.log('validateFields:', val1);
                  const val2 = await formRef.current?.validateFieldsReturnFormatValue?.();
                  console.log('validateFieldsReturnFormatValue:', val2);
                  message.success('提交成功');*/
                  console.log('基本信息values: ', values);
                  /*const genTable = Object.assign({}, values, genForm.model);
                  genTable.columns = this.columns;*/
                  // setBasicForm({...values});
                }}
                formRef={formRef}
                params={{ id: '100' }}
                formKey="base-form-use-demo"
                submitter={false}
                onValuesChange={(values) => {
                  setBasicForm({ ...basicForm, ...values });
                }}
                /*request={async () => {
                  await waitTime(100);
                  return {
                    name: '蚂蚁设计有限公司',
                    useMode: 'chapter',
                  };
                }}*/
                autoFocusFirstInput
              >
                <ProForm.Group>
                  <ProFormText
                    initialValue={currentRow?.tableName}
                    width="md"
                    name="tableName"
                    required
                    label="表名称"
                    placeholder="请输入表名称"
                    rules={[{ required: true, message: '这是必填项' }]}
                  />
                  <ProFormText
                    initialValue={currentRow?.tableComment}
                    width="md"
                    name="tableComment"
                    label="表描述"
                    placeholder="请输入表描述"
                    required
                    rules={[{ required: true, message: '这是必填项' }]}
                  />
                </ProForm.Group>

                <ProForm.Group>
                  <ProFormText
                    initialValue={currentRow?.className}
                    width="md"
                    name="className"
                    required
                    label="实体类名称"
                    placeholder="请输入实体类名称"
                    rules={[{ required: true, message: '这是必填项' }]}
                  />
                  <ProFormText
                    initialValue={currentRow?.functionAuthor}
                    width="md"
                    name="functionAuthor"
                    label="作者"
                    placeholder="请输入作者"
                    required
                    rules={[{ required: true, message: '这是必填项' }]}
                  />
                </ProForm.Group>
                <ProFormTextArea
                  initialValue={currentRow?.remark}
                  width={'md'}
                  name={'remark'}
                  label={'备注'}
                />
              </ProForm>
            </TabPane>
            <TabPane tab="字段信息" key="2">
              <EditTable
                values={tableColumn}
                onChange={(values) => {
                  setTableColumns(values);
                }}
              />
            </TabPane>
            <TabPane tab="生成信息" key="3">
              <ProForm<{
                tplCategory?: string;
                packageName?: string;
                moduleName?: string;
                businessName?: string;
                functionName?: string;
                parentMenuId?: string;
                genType?: string;
                genPath?: string;
              }>
                onFinish={async (values) => {
                  /*await waitTime(2000);
                  console.log(values);
                  const val1 = await formRef.current?.validateFields();
                  console.log('validateFields:', val1);
                  const val2 = await formRef.current?.validateFieldsReturnFormatValue?.();
                  console.log('validateFieldsReturnFormatValue:', val2);
                  message.success('提交成功');*/
                  console.log('生成信息values: ', values);
                  // setGenForm({...values});
                }}
                formRef={genFormRef}
                params={{ id: '100' }}
                formKey="base-form-use-demo1"
                /*request={async () => {
                  await waitTime(100);
                  return {
                    name: '蚂蚁设计有限公司',
                    useMode: 'chapter',
                  };
                }}*/
                autoFocusFirstInput
                submitter={false}
                onValuesChange={(values) => {
                  console.log(genFormRef.current?.getFieldsValue().genType);
                  setGenForm({ ...genForm, ...values });
                }}
              >
                <ProForm.Group>
                  <ProFormText
                    initialValue={currentRow?.tplCategory}
                    width="md"
                    name="tplCategory"
                    required
                    label="生成模板"
                    placeholder="请输入生成模板"
                    rules={[{ required: true, message: '这是必填项' }]}
                  />
                  <ProFormText
                    initialValue={currentRow?.packageName}
                    width="md"
                    name="packageName"
                    label="生成包路径"
                    placeholder="请输入生成路径"
                    required
                    rules={[{ required: true, message: '这是必填项' }]}
                  />
                </ProForm.Group>

                <ProForm.Group>
                  <ProFormText
                    initialValue={currentRow?.moduleName}
                    width="md"
                    name="moduleName"
                    required
                    label="生成模块名"
                    placeholder="请输入生成模块名"
                    rules={[{ required: true, message: '这是必填项' }]}
                  />
                  <ProFormText
                    initialValue={currentRow?.businessName}
                    width="md"
                    name="businessName"
                    label="生成业务名"
                    placeholder="请输入生成业务名"
                    required
                    rules={[{ required: true, message: '这是必填项' }]}
                  />
                </ProForm.Group>
                <ProForm.Group>
                  <ProFormText
                    initialValue={currentRow?.functionName}
                    width="md"
                    name="functionName"
                    required
                    label="生成功能名"
                    placeholder="请输入生成功能名"
                    rules={[{ required: true, message: '这是必填项' }]}
                  />
                  {/* <ProFormText
                    initialValue={currentRow?.parentMenuId}
                    width="md"
                    name="parentMenuId"
                    label="上级菜单"
                    placeholder="请输入上级菜单"
                  /> */}
                </ProForm.Group>
                <ProFormRadio.Group
                  initialValue={currentRow?.genType || '0'}
                  name="genType"
                  label="生成方式"
                  options={[
                    {
                      label: 'zip压缩包',
                      value: '0',
                    },
                    {
                      label: '自定义路径',
                      value: '1',
                    },
                  ]}
                />
                {genFormRef.current?.getFieldsValue().genType === '1' && (
                  <ProForm.Group>
                    <ProFormText
                      initialValue={currentRow?.genPath}
                      width="md"
                      name="genPath"
                      label="自定义路径"
                      placeholder="请输入自定义路径"
                    />
                  </ProForm.Group>
                )}
              </ProForm>
            </TabPane>
          </Tabs>
        </Modal>

        <Drawer
          width={600}
          visible={showDetail}
          onClose={() => {
            setCurrentRow(undefined);
            setShowDetail(false);
          }}
          closable={false}
        >
          {currentRow?.tableName && (
            <ProDescriptions<Tool.GenTable>
              column={2}
              title={currentRow?.tableName}
              request={async () => ({
                data: currentRow || {},
              })}
              params={{
                id: currentRow?.tableName,
              }}
              columns={columns as ProDescriptionsItemProps<Tool.GenTable>[]}
            />
          )}
        </Drawer>
      </>
    </PageContainer>
  );
};

export default Generator;
