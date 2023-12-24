import React, { useEffect, useState } from 'react';
import { Button } from 'antd';

import { EditableProTable, ProColumns } from '@ant-design/pro-table';
import { dictTypeOptionSelect } from '@/services/system/sysDict';
import { SYSTEM } from '@/services/system/typings';

export type EditTableProps = {
  /*onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalVisible: boolean;*/
  onChange: (values: Tool.GenTableColumn[]) => void;
  values: Tool.GenTableColumn[];
};

const EditTable: React.FC<EditTableProps> = (props) => {
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>(() =>
    props.values.map((item) => item?.columnId || 0),
  );
  const [dataSource, setDataSource] = useState<Tool.GenTableColumn[]>(() => props.values);
  const [dictTypeOption, setOictTypeOption] = useState<SYSTEM.SysDictType[]>([]);

  console.log('values: ', props.values);

  useEffect(() => {
    dictTypeOptionSelect().then((res) => {
      setOictTypeOption(res.data);
    });
  }, []);

  const columns: ProColumns<Tool.GenTableColumn>[] = [
    {
      title: '字段名称',
      dataIndex: 'columnName',
      width: '160px',
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '字段描述',
      key: 'columnComment',
      dataIndex: 'columnComment',
      width: '180px',
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '物理类型',
      key: 'columnType',
      dataIndex: 'columnType',
      width: '180px',
      editable: false,
    },
    {
      title: 'Java类型',
      key: 'javaType',
      dataIndex: 'javaType',
      valueType: 'select',
      width: '109px',
      valueEnum: {
        Long: { text: 'Long', status: 'Default' },
        String: {
          text: 'String',
          status: 'Default',
        },
        Integer: {
          text: 'Integer',
          status: 'Default',
        },
        Double: {
          text: 'Double',
          status: 'Default',
        },
        BigDecimal: {
          text: 'BigDecimal',
          status: 'Default',
        },
        Date: {
          text: 'Date',
          status: 'Default',
        },
        Boolean: {
          text: 'Boolean',
          status: 'Default',
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: 'Java属性',
      key: 'javaField',
      dataIndex: 'javaField',
      width: '180px',
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: 'TypeScript类型',
      key: 'typeScriptType',
      dataIndex: 'typeScriptType',
      valueType: 'select',
      width: '109px',
      valueEnum: {
        string: { text: 'string', status: 'Default' },
        number: {
          text: 'number',
          status: 'Default',
        },
        boolean: {
          text: 'boolean',
          status: 'Default',
        },
        any: {
          text: 'any',
          status: 'Default',
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '插入',
      key: 'isInsert',
      dataIndex: 'isInsert',
      valueType: 'checkbox',
      width: '80px',
      valueEnum: {
        '1': {
          text: true,
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '编辑',
      key: 'isEdit',
      dataIndex: 'isEdit',
      valueType: 'checkbox',
      width: '80px',
      valueEnum: {
        '1': {
          text: true,
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '列表',
      key: 'isList',
      dataIndex: 'isList',
      valueType: 'checkbox',
      width: '80px',
      valueEnum: {
        '1': {
          text: true,
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '查询',
      key: 'isQuery',
      dataIndex: 'isQuery',
      valueType: 'checkbox',
      width: '80px',
      valueEnum: {
        '1': {
          text: true,
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '查询方式',
      key: 'queryType',
      dataIndex: 'queryType',
      valueType: 'select',
      width: '180px',
      valueEnum: {
        EQ: { text: '=', status: 'Default' },
        NE: {
          text: '!=',
          status: 'Default',
        },
        GT: {
          text: '>',
          status: 'Default',
        },
        GTE: {
          text: '>=',
          status: 'Default',
        },
        LT: {
          text: '<=',
          status: 'Default',
        },
        LIKE: {
          text: 'LIKE',
          status: 'Default',
        },
        BETWEEN: {
          text: 'BETWEEN',
          status: 'Default',
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '必填',
      key: 'isRequired',
      dataIndex: 'isRequired',
      valueType: 'checkbox',
      width: '80px',
      valueEnum: {
        '1': {
          text: true,
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '显示类型',
      key: 'htmlType',
      dataIndex: 'htmlType',
      valueType: 'select',
      width: '180px',
      valueEnum: {
        input: { text: '文本框', status: 'Default' },
        textarea: {
          text: '文本域',
          status: 'Default',
        },
        select: {
          text: '下拉框',
          status: 'Default',
        },
        radio: {
          text: '单选框',
          status: 'Default',
        },
        checkbox: {
          text: '复选框',
          status: 'Default',
        },
        datetime: {
          text: '日期控件',
          status: 'Default',
        },
        imageUpload: {
          text: '图片上传',
          status: 'Default',
        },
        fileUpload: {
          text: '文件上传',
          status: 'Default',
        },
        editor: {
          text: '富文本控件',
          status: 'Default',
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
    {
      title: '字典类型',
      key: 'dictType',
      dataIndex: 'dictType',
      valueType: 'select',
      width: '180px',
      valueEnum: () => {
        const enumObj = {};
        dictTypeOption.forEach((ele: any) => {
          enumObj[ele.dictType] = { text: `${ele.dictName}-${ele.dictType}`, status: 'Default' };
        });
        return enumObj;
      },
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          },
        ],
      },
    },
  ];

  return (
    <>
      <EditableProTable<Tool.GenTableColumn>
        headerTitle="编辑列属性"
        columns={columns}
        rowKey="columnId"
        scroll={{
          x: 960,
        }}
        value={dataSource}
        onChange={setDataSource}
        /*recordCreatorProps={{
            newRecordType: 'dataSource',
            record: () => ({
              id: Date.now(),
            }),
          }}*/
        toolBarRender={() => {
          return [
            <Button
              type="primary"
              key="save"
              onClick={() => {
                // dataSource 就是当前数据，可以调用 api 将其保存
                console.log(dataSource);
                props.onChange(dataSource);
              }}
            >
              保存数据
            </Button>,
          ];
        }}
        editable={{
          type: 'multiple',
          editableKeys,
          actionRender: (row, config, defaultDoms) => {
            console.log('row, config, defaultDoms): ', row, config, defaultDoms);
            return [defaultDoms.delete];
          },
          onValuesChange: (record, recordList) => {
            setDataSource(recordList);
          },
          onChange: setEditableRowKeys,
        }}
        recordCreatorProps={false}
      />
    </>
  );
};

export default EditTable;
