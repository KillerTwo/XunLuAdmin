import React, { useEffect, useRef, useState } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {
  Button,
  Divider,
  Drawer,
  Form,
  Input,
  message,
  Modal,
  Popconfirm,
  Select,
  Space,
  Tag,
  Tooltip,
  Typography,
} from 'antd';
import { ModalForm, ProFormSelect, ProFormText, ProFormTextArea } from '@ant-design/pro-form';
import {
  oauth2ClientList,
  addOAuth2Client,
  updateOAuth2Client,
  removeOAuth2Client,
  getOAuth2Client,
  getOAuth2Enums,
} from '@/services/system/oauth2Client';
import type { SYSTEM } from '@/services/system/typings';

const { Paragraph } = Typography;
const { TextArea } = Input;

/**
 * OAuth2客户端管理页面
 * 参考GitHub OAuth Apps设计
 */
const OAuth2ClientList: React.FC = () => {
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<SYSTEM.OAuth2Client>();
  const [enums, setEnums] = useState<SYSTEM.OAuth2Enums>();
  const actionRef = useRef<ActionType>();
  const [form] = Form.useForm();

  // 加载枚举数据
  useEffect(() => {
    getOAuth2Enums().then((res) => {
      if (res.code === 200) {
        setEnums(res.data);
      }
    });
  }, []);

  // 新增客户端
  const handleAdd = async (fields: SYSTEM.OAuth2Client) => {
    const hide = message.loading('正在添加');
    try {
      // 处理 redirectUris：将换行转换为逗号分隔
      const processedFields = {
        ...fields,
        redirectUris: fields.redirectUris
          ? fields.redirectUris.split('\n').map(uri => uri.trim()).filter(uri => uri).join(',')
          : ''
      };

      await addOAuth2Client(processedFields);
      hide();
      message.success('添加成功！');
      return true;
    } catch (error) {
      hide();
      message.error('添加失败，请重试!');
      return false;
    }
  };

  // 更新客户端
  const handleUpdate = async (fields: SYSTEM.OAuth2Client) => {
    const hide = message.loading('正在更新');
    try {
      // 处理 redirectUris：将换行转换为逗号分隔
      const processedFields = {
        ...currentRow,
        ...fields,
        redirectUris: fields.redirectUris
          ? fields.redirectUris.split('\n').map(uri => uri.trim()).filter(uri => uri).join(',')
          : ''
      };

      await updateOAuth2Client(processedFields);
      hide();
      message.success('更新成功');
      return true;
    } catch (error) {
      hide();
      message.error('更新失败，请重试!');
      return false;
    }
  };

  // 删除客户端
  const handleRemove = async (selectedRows: SYSTEM.OAuth2Client[]) => {
    const hide = message.loading('正在删除');
    if (!selectedRows) return true;
    try {
      await removeOAuth2Client(selectedRows.map((row) => row.id!));
      hide();
      message.success('删除成功');
      return true;
    } catch (error) {
      hide();
      message.error('删除失败，请重试');
      return false;
    }
  };

  // 表格列定义
  const columns: ProColumns<SYSTEM.OAuth2Client>[] = [
    {
      title: '应用名称',
      dataIndex: 'clientName',
      tip: '应用的显示名称',
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
      title: 'Client ID',
      dataIndex: 'clientId',
      copyable: true,
      ellipsis: true,
      width: 200,
    },
    {
      title: 'Client Secret',
      dataIndex: 'clientSecret',
      hideInSearch: true,
      render: (text) => {
        return text ? <Tag color="blue">已配置</Tag> : <Tag>无</Tag>;
      },
    },
    {
      title: '授权类型',
      dataIndex: 'authorizationGrantTypes',
      hideInSearch: true,
      render: (_, record) => (
        <Space wrap>
          {record.authorizationGrantTypes?.map((type) => (
            <Tag key={type} color="processing">
              {type}
            </Tag>
          ))}
        </Space>
      ),
    },
    {
      title: '认证方式',
      dataIndex: 'clientAuthenticationMethods',
      hideInSearch: true,
      render: (text) => <Tag color="cyan">{text}</Tag>,
    },
    {
      title: '创建时间',
      dataIndex: 'clientIdIssuedAt',
      valueType: 'dateTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="edit"
          onClick={async () => {
            const res = await getOAuth2Client(record.id!);
            if (res.code === 200) {
              setCurrentRow(res.data);
              setUpdateModalVisible(true);
            }
          }}
        >
          编辑
        </a>,
        <Divider key="divider1" type="vertical" />,
        <Popconfirm
          key="delete"
          title="确定要删除这个客户端吗？"
          onConfirm={async () => {
            await handleRemove([record]);
            actionRef.current?.reloadAndRest?.();
          }}
        >
          <a style={{ color: 'red' }}>删除</a>
        </Popconfirm>,
      ],
    },
  ];

  return (
    <div>
      <ProTable<SYSTEM.OAuth2Client, SYSTEM.PageParams>
        headerTitle="OAuth2 客户端应用"
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            icon={<PlusOutlined />}
            onClick={() => {
              setCreateModalVisible(true);
            }}
          >
            新建应用
          </Button>,
        ]}
        request={async (params, sorter, filter) => {
          const res = await oauth2ClientList({
            pageNum: params.current,
            pageSize: params.pageSize,
            clientId: params.clientId,
            clientName: params.clientName,
          });
          return {
            data: res.rows || [],
            success: res.code === 200,
            total: res.total || 0,
          };
        }}
        columns={columns}
      />

      {/* 新建表单 */}
      <ModalForm
        title="新建 OAuth2 应用"
        width="600px"
        visible={createModalVisible}
        onVisibleChange={setCreateModalVisible}
        modalProps={{
          destroyOnClose: true,
        }}
        onFinish={async (value) => {
          const success = await handleAdd(value as SYSTEM.OAuth2Client);
          if (success) {
            setCreateModalVisible(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormText
          label="应用名称"
          name="clientName"
          rules={[{ required: true, message: '请输入应用名称！' }]}
          placeholder="例如：我的应用"
          tooltip="用户看到的应用名称"
        />

        <ProFormText
          label="Client ID"
          name="clientId"
          rules={[{ required: true, message: '请输入 Client ID！' }]}
          placeholder="例如：my-app-client"
          tooltip="客户端唯一标识符，建议使用小写字母和连字符"
        />

        <ProFormText.Password
          label="Client Secret"
          name="clientSecret"
          placeholder="留空表示公共客户端（不需要密钥）"
          tooltip="机密客户端需要密钥，公共客户端（如SPA）不需要"
        />

        <ProFormSelect
          name="clientAuthenticationMethods"
          label="认证方式"
          tooltip="客户端如何向认证服务器证明身份"
          rules={[{ required: true, message: '请选择认证方式！' }]}
          options={enums?.clientAuthenticationMethods.map((item) => ({
            label: item,
            value: item,
          }))}
        />

        <ProFormSelect
          name="authorizationGrantTypes"
          label="授权类型"
          tooltip="客户端支持的OAuth2授权流程"
          mode="multiple"
          rules={[{ required: true, message: '请选择至少一个授权类型！' }]}
          options={enums?.authorizationGrantTypes.map((item) => ({
            label: item,
            value: item,
          }))}
        />

        <ProFormSelect
          name="scopes"
          label="授权范围"
          tooltip="客户端可以请求的权限范围"
          mode="multiple"
          rules={[{ required: true, message: '请选择至少一个授权范围！' }]}
          options={enums?.scopes.map((item) => ({
            label: item,
            value: item,
          }))}
        />

        <ProFormTextArea
          label="回调地址"
          name="redirectUris"
          placeholder="每行一个URL，例如：&#10;https://example.com/callback&#10;http://localhost:3000/callback"
          tooltip="授权成功后跳转的地址，每行一个"
          fieldProps={{
            rows: 4,
          }}
        />
      </ModalForm>

      {/* 编辑表单 */}
      {updateModalVisible && currentRow && (
        <ModalForm
          title="编辑 OAuth2 应用"
          width="600px"
          visible={updateModalVisible}
          onVisibleChange={setUpdateModalVisible}
          initialValues={{
            ...currentRow,
            // 将逗号分隔的 redirectUris 转换为换行分隔
            redirectUris: currentRow.redirectUris
              ? currentRow.redirectUris.split(',').map(uri => uri.trim()).join('\n')
              : ''
          }}
          modalProps={{
            destroyOnClose: true,
          }}
          onFinish={async (value) => {
            const success = await handleUpdate(value as SYSTEM.OAuth2Client);
            if (success) {
              setUpdateModalVisible(false);
              setCurrentRow(undefined);
              if (actionRef.current) {
                actionRef.current.reload();
              }
            }
          }}
        >
          <ProFormText name="id" hidden />

          <ProFormText
            label="应用名称"
            name="clientName"
            rules={[{ required: true, message: '请输入应用名称！' }]}
          />

          <ProFormText
            label="Client ID"
            name="clientId"
            disabled
            tooltip="Client ID 不可修改"
          />

          <ProFormSelect
            name="clientAuthenticationMethods"
            label="认证方式"
            rules={[{ required: true, message: '请选择认证方式！' }]}
            options={enums?.clientAuthenticationMethods.map((item) => ({
              label: item,
              value: item,
            }))}
          />

          <ProFormSelect
            name="authorizationGrantTypes"
            label="授权类型"
            mode="multiple"
            rules={[{ required: true, message: '请选择至少一个授权类型！' }]}
            options={enums?.authorizationGrantTypes.map((item) => ({
              label: item,
              value: item,
            }))}
          />

          <ProFormSelect
            name="scopes"
            label="授权范围"
            mode="multiple"
            rules={[{ required: true, message: '请选择至少一个授权范围！' }]}
            options={enums?.scopes.map((item) => ({
              label: item,
              value: item,
            }))}
          />

          <ProFormTextArea
            label="回调地址"
            name="redirectUris"
            placeholder="每行一个URL"
            fieldProps={{
              rows: 4,
            }}
          />
        </ModalForm>
      )}

      {/* 详情抽屉 */}
      <Drawer
        width={600}
        visible={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.clientName && (
          <>
            <h2>{currentRow.clientName}</h2>
            <Divider />
            <div style={{ marginBottom: 16 }}>
              <p style={{ fontWeight: 'bold' }}>Client ID</p>
              <Paragraph copyable>{currentRow.clientId}</Paragraph>
            </div>
            <div style={{ marginBottom: 16 }}>
              <p style={{ fontWeight: 'bold' }}>认证方式</p>
              <Tag color="cyan">{currentRow.clientAuthenticationMethods}</Tag>
            </div>
            <div style={{ marginBottom: 16 }}>
              <p style={{ fontWeight: 'bold' }}>授权类型</p>
              <Space wrap>
                {currentRow.authorizationGrantTypes?.map((type) => (
                  <Tag key={type} color="processing">
                    {type}
                  </Tag>
                ))}
              </Space>
            </div>
            <div style={{ marginBottom: 16 }}>
              <p style={{ fontWeight: 'bold' }}>授权范围</p>
              <Space wrap>
                {currentRow.scopes?.map((scope) => (
                  <Tag key={scope} color="green">
                    {scope}
                  </Tag>
                ))}
              </Space>
            </div>
            <div style={{ marginBottom: 16 }}>
              <p style={{ fontWeight: 'bold' }}>回调地址</p>
              <Paragraph>
                {currentRow.redirectUris?.split(',').map((uri, index) => (
                  <div key={index}>{uri.trim()}</div>
                ))}
              </Paragraph>
            </div>
            <div style={{ marginBottom: 16 }}>
              <p style={{ fontWeight: 'bold' }}>创建时间</p>
              <p>{currentRow.clientIdIssuedAt}</p>
            </div>
          </>
        )}
      </Drawer>
    </div>
  );
};

export default OAuth2ClientList;
