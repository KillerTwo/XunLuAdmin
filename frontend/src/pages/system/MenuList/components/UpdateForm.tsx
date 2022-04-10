import React, {useEffect, useState} from 'react';
import {
  ProFormText,
  ModalForm, ProForm, ProFormTreeSelect, ProFormRadio, ProFormDigit, ProFormSelect,
} from '@ant-design/pro-form';
import {SYSTEM} from "@/services/system/typings";
import {sysMenuSelectList} from "@/services/system/sysMenu";
import {TreeSelect} from "antd";
import {iconSelect} from "@/utils/routes";

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<SYSTEM.SysMenu>;

export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;
  values: Partial<SYSTEM.SysMenu>;
};



const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  const [menuType, setMenuType] = useState<string|undefined>("");

  useEffect(() => {
    setMenuType(props.values.menuType);
  }, [props.values])

  return (
    <ModalForm
      modalProps={{destroyOnClose: true}}
      title={'修改菜单'}
      width="60%"
      visible={props.updateModalVisible}
      onVisibleChange={props.onCancel}
      onFinish={props.onSubmit}
      onValuesChange={(changedValues) => {
        if (changedValues["menuType"]) {
          setMenuType(changedValues["menuType"]);
        }
      }}
    >
      <ProForm.Group>
        <ProFormTreeSelect
          initialValue={props.values.parentId}
          width={"md"}
          label="上级菜单"
          request={async () => {
            const resData = await sysMenuSelectList();
            const menu: {id: number, label: string, children: []} = { id: 0, label: '主类目', children: [] };
            menu.children = resData.data;
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
          initialValue={props.values.menuType}
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
          initialValue={props.values.icon}
          width="md"
          name="icon"
          label={"菜单图标"}
        />*/}
        <ProFormSelect
          initialValue={props.values.icon}
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
          initialValue={props.values.menuName}
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
          initialValue={props.values.orderNum}
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
                initialValue={props.values.isFrame}
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
                initialValue={props.values.path}
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
                      initialValue={props.values.component}
                      width="md"
                      name="component"
                      label={"组件路径"}
                    />
                    <ProFormText
                      initialValue={props.values.path}
                      width="md"
                      name="perms"
                      label={"权限字符串"}
                    />
                  </ProForm.Group>
                  <ProForm.Group>
                    <ProFormText
                      initialValue={props.values.query}
                      width="md"
                      name="query"
                      label={"路由参数"}
                    />
                    <ProFormRadio.Group
                      initialValue={props.values.isCache}
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
                initialValue={props.values.visible}
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
                initialValue={props.values.status}
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
  );
};

export default UpdateForm;
