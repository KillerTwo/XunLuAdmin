<script lang="ts" setup>
// import { SmileOutlined, DownOutlined } from '@vben/icons';
import type { DrawerProps, SelectProps, TableProps } from 'ant-design-vue';

import { computed, reactive, ref, watch, watchEffect } from 'vue';
import type { Ref, UnwrapRef } from 'vue';

// import {Table, Button} from "ant-design-vue";
import { Page } from '@vben/common-ui';
import { MaterialCheck, MaterialEdit } from '@vben/icons';

import { cloneDeep } from 'lodash-es';

import { requestClient } from '#/api/request';

const tableName = ref<string>('');

const current = ref<number>(1);
const pageSize = ref<number>(10);
const datasource = ref<any[]>([]);
const loading = ref<boolean>(false);

const placement = ref<DrawerProps['placement']>('right');
const open = ref<boolean>(false);

watch(tableName, () => {
  // console.log(tableName.value);
});

interface ColumnType {
  columnComment: string;
  columnId: number;
  columnName: string;
  columnType: string;
  dictType: string;
  htmlType: string;
  isIncrement: string;
  isPk: string;
  isRequired: boolean;
  javaField: string;
  javaType: string;
  sort: number;
  typeScriptType: string;
}

const columns = [
  {
    dataIndex: 'tableName',
    key: 'tableName',
    title: '表名',
  },
  {
    dataIndex: 'tableComment',
    key: 'tableComment',
    title: '表说明',
  },
  {
    dataIndex: 'className',
    key: 'className',
    title: '类名',
  },
  {
    dataIndex: 'tplCategory',
    key: 'tplCategory',
    title: '生成类型',
  },
  {
    key: 'action',
    title: '操作',
  },
];

const editColumns = [
  {
    dataIndex: 'columnId',
    key: 'columnId',
    minWidth: 100,
    title: '编号',
    width: 100,
  },
  {
    dataIndex: 'columnName',
    key: 'columnName',
    title: '字段名',
    width: 100,
  },
  {
    dataIndex: 'columnComment',
    key: 'columnComment',
    title: '字段说明',
    width: 100,
  },
  {
    dataIndex: 'columnType',
    key: 'columnType',
    title: '字段类型',
    width: 100,
  },
  {
    dataIndex: 'javaField',
    key: 'javaField',
    title: '属性名',
    width: 100,
  },
  {
    dataIndex: 'javaType',
    key: 'javaType',
    title: '属性类型',
    width: 100,
  },
  {
    dataIndex: 'typeScriptType',
    key: 'typeScriptType',
    title: 'TypeScript类型',
    width: 100,
  },
  {
    dataIndex: 'isPk',
    key: 'isPk',
    title: '是否主键',
    width: 100,
  },
  {
    dataIndex: 'isIncrement',
    key: 'isIncrement',
    title: '是否自增',
    width: 100,
  },
  {
    dataIndex: 'isRequired',
    key: 'isRequired',
    title: '是否必填',
    width: 100,
  },
  {
    dataIndex: 'htmlType',
    key: 'htmlType',
    title: '显示类型',
    width: 100,
  },
  {
    dataIndex: 'dictType',
    key: 'dictType',
    title: '字典类型',
    width: 100,
  },
  {
    dataIndex: 'sort',
    key: 'sort',
    title: '排序',
    width: 100,
  },
];

const dataSource: Ref<ColumnType[]> = ref([
  {
    columnComment: '用户名',
    columnId: 1,
    columnName: 'username',
    columnType: 'VARCHAR(20)',
    dictType: '1',
    htmlType: 'input',
    isIncrement: '否',
    isPk: '否',
    isRequired: true,
    javaField: 'username',
    javaType: 'String',
    sort: 1,
    typeScriptType: 'string',
  },
  {
    columnComment: '密码',
    columnId: 2,
    columnName: 'password',
    columnType: 'VARCHAR(20)',
    dictType: '1',
    htmlType: 'input',
    isIncrement: '否',
    isPk: '否',
    isRequired: true,
    javaField: 'password',
    javaType: 'String',
    sort: 2,
    typeScriptType: 'string',
  },
  {
    columnComment: '性别',
    columnId: 3,
    columnName: 'sex',
    columnType: 'VARCHAR(20)',
    dictType: '1',
    htmlType: 'input',
    isIncrement: '否',
    isPk: '否',
    isRequired: false,
    javaField: 'sex',
    javaType: 'String',
    sort: 3,
    typeScriptType: 'string',
  },
  {
    columnComment: '年龄',
    columnId: 4,
    columnName: 'age',
    columnType: 'VARCHAR(20)',
    dictType: '1',
    htmlType: 'input',
    isIncrement: '否',
    isPk: '否',
    isRequired: false,
    javaField: 'age',
    javaType: 'String',
    sort: 4,
    typeScriptType: 'string',
  },
]);

/*
type APIParams = {
  results: number;
  page?: number;
  sortField?: string;
  sortOrder?: number;
  [key: string]: any;
};*/

type APIResult = {
  results: {
    className: string;
    tableComment: string;
    tableName: string;
    tplCategory: string[];
  }[];
};

watchEffect(async () => {
  // 该 effect 会立即运行，
  // 并且在 currentBranch.value 改变时重新运行
  const url = `http://localhost:5666/api/table/list?page=${current.value}`;
  const res = await requestClient.get<APIResult>(url);
  datasource.value = res.results;
});

const pagination = computed(() => ({
  current: current.value,
  pageSize: pageSize.value,
  total: 200,
}));

const handleTableChange: TableProps['onChange'] = async (
  pag: { current: number; pageSize: number },
  /*  filters: any,
  sorter: any,*/
) => {
  // datasource.value = await queryData({ page: current.value, results: 10 });
  current.value = pag.current;
  pageSize.value = pag.pageSize;
};

type Key = number | string;

const state = reactive<{
  loading: boolean;
  selectedRowKeys: Key[];
}>({
  loading: false,
  selectedRowKeys: [], // Check here to configure the default column
});

const onSelectChange = (selectedRowKeys: Key[]) => {
  state.selectedRowKeys = selectedRowKeys;
};

const showDrawer = () => {
  open.value = true;
};

const onClose = () => {
  open.value = false;
};

const editableData: UnwrapRef<Record<string, ColumnType>> = reactive({});

const edit = (columnId: number, key: string) => {
  // editableData[key] = cloneDeep(dataSource.value.find((item) => key === item.columnId));

  const t = editableData[`${columnId}_${key}`];
  if (t) {
    return;
  }
  editableData[`${columnId}_${key}`] = cloneDeep(
    dataSource.value.find((item) => columnId === item.columnId),
  );
};
const save = (columnId: number, key: string) => {
  const d = dataSource.value.find((item) => columnId === item.columnId);
  Object.assign(d, { ...d, [key]: editableData[`${columnId}_${key}`][key] });
  delete editableData[`${columnId}_${key}`];
};
const handleJavaTypeChange = (value: string) => {
  console.log(`selected ${value}`);
};

const handleJavascriptTypeChange = (value: string) => {
  console.log(`selected ${value}`);
};

const handleHtmlTypeChange = (value: string) => {
  console.log(`selected ${value}`);
};

const handleBlur = () => {
  console.log('blur');
};
const handleFocus = () => {
  console.log('focus');
};

const javaTypeOptions = ref<SelectProps['options']>([
  { label: 'Integer', value: 'Integer' },
  { label: 'Float', value: 'Float' },
  { label: 'Long', value: 'Long' },
  { label: 'Boolean', value: 'Boolean' },
  { label: 'String', value: 'String' },
  { label: 'Double', value: 'Double' },
  { label: 'Date', value: 'Date' },
  { label: 'LocalDate', value: 'LocalDate' },
  { label: 'LocalTime', value: 'LocalTime' },
  { label: 'LocalDateTime', value: 'LocalDateTime' },
  { label: 'Object', value: 'Object' },
]);

const javaScriptTypeOptions = ref<SelectProps['options']>([
  { label: 'Integer', value: 'Integer' },
  { label: 'Float', value: 'Float' },
  { label: 'Long', value: 'Long' },
  { label: 'Boolean', value: 'Boolean' },
  { label: 'String', value: 'String' },
  { label: 'Double', value: 'Double' },
  { label: 'Date', value: 'Date' },
  { label: 'LocalDate', value: 'LocalDate' },
  { label: 'LocalTime', value: 'LocalTime' },
  { label: 'LocalDateTime', value: 'LocalDateTime' },
  { label: 'Object', value: 'Object' },
]);

const htmlTypeOptions = ref<SelectProps['options']>([
  { label: '文本框', value: 'input' },
  { label: '文本域', value: 'textarea' },
  { label: '下拉框', value: 'select' },
  { label: '单选框', value: 'radio' },
  { label: '复选框', value: 'checkbox' },
  { label: '日期控件', value: 'datetime' },
  { label: '图片上传', value: 'image' },
  { label: '文本上传', value: 'file' },
  { label: '富文本控件', value: 'richtextarea' },
]);

const htmlTypeFilter = (value: string) => {

}

const filterOption = (input: string, option: any) => {
  return option.value.toLowerCase().includes(input.toLowerCase());
};
</script>

<template>
  <div>
    <Page description="配置代码生成" title="代码生成">
      <a-space style="margin-bottom: 10px">
        <a-input v-model:value="tableName" placeholder="表名" />
        <a-button>搜索</a-button>
        <a-button type="primary">导入</a-button>
        <a-button type="primary">生成代码</a-button>
        <a-button danger>删除</a-button>
      </a-space>

      <a-table
        :columns="columns"
        :data-source="datasource"
        :loading="loading"
        :pagination="pagination"
        :row-key="(record) => record.id"
        :row-selection="{
          selectedRowKeys: state.selectedRowKeys,
          onChange: onSelectChange,
        }"
        bordered
        size="small"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, text }">
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button @click="showDrawer">编辑</a-button>
              <a-button>预览</a-button>
              <a-button>生成代码</a-button>
              <a-button>删除</a-button>
              <a-button>同步</a-button>
            </a-space>
          </template>
          <template v-else-if="column.key === 'tplCategory'">
            <a-tag color="cyan">{{ text[0] }}</a-tag>
          </template>
          <template v-else>{{ text }}</template>
        </template>
      </a-table>
    </Page>
    <a-drawer
      :closable="false"
      :destroy-on-close="true"
      :mask-closable="false"
      :open="open"
      :placement="placement"
      size="large"
      title="编辑"
      width="100%"
      @close="onClose"
    >
      <template #extra>
        <span
          class="iconify"
          data-icon="material-symbols:check"
          data-inline="false"
        ></span>
        <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
        <a-button type="primary" @click="onClose">确定</a-button>
      </template>
      <a-table
        :columns="editColumns"
        :data-source="dataSource"
        bordered
        size="small"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.key === 'columnComment'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_columnComment`]"
                class="editable-cell-input-wrapper"
              >
                <a-space style="margin-bottom: 10px">
                  <a-input
                    v-model:value="
                      editableData[`${record.columnId}_columnComment`]
                        .columnComment
                    "
                    @press-enter="save(record.columnId, 'columnComment')"
                  />
                  <MaterialCheck
                    @click="save(record.columnId, 'columnComment')"
                  />
                </a-space>
              </div>
              <div v-else class="editable-cell-text-wrapper">
                <a-space style="margin-bottom: 10px">
                  {{ text || ' ' }}
                  <MaterialEdit
                    @click="edit(record.columnId, 'columnComment')"
                  />
                </a-space>
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'javaField'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_javaField`]"
                class="editable-cell-input-wrapper"
              >
                <a-space style="margin-bottom: 10px">
                  <a-input
                    v-model:value="
                      editableData[`${record.columnId}_javaField`].javaField
                    "
                    @press-enter="save(record.columnId, 'javaField')"
                  />
                  <MaterialCheck @click="save(record.columnId, 'javaField')" />
                </a-space>
              </div>
              <div v-else class="editable-cell-text-wrapper">
                <a-space style="margin-bottom: 10px">
                  {{ text || ' ' }}
                  <MaterialEdit @click="edit(record.columnId, 'javaField')" />
                </a-space>
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'javaType'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_javaType`]"
                class="editable-cell-input-wrapper"
              >
                <a-space style="margin-bottom: 10px">
                  <a-select
                    v-model:value="
                      editableData[`${record.columnId}_javaType`].javaType
                    "
                    :filter-option="filterOption"
                    :options="javaTypeOptions"
                    placeholder="选择java类型"
                    show-search
                    style="width: 100px"
                    @blur="handleBlur"
                    @change="handleJavaTypeChange"
                    @focus="handleFocus"
                    @press-enter="save(record.columnId, 'javaType')"
                  />
                  <MaterialCheck @click="save(record.columnId, 'javaType')" />
                </a-space>
              </div>
              <div v-else class="editable-cell-text-wrapper">
                <a-space style="margin-bottom: 10px">
                  {{ text || ' ' }}
                  <MaterialEdit @click="edit(record.columnId, 'javaType')" />
                </a-space>
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'typeScriptType'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_typeScriptType`]"
                class="editable-cell-input-wrapper"
              >
                <a-space style="margin-bottom: 10px">
                  <a-select
                    v-model:value="
                      editableData[`${record.columnId}_typeScriptType`]
                        .typeScriptType
                    "
                    :filter-option="filterOption"
                    :options="javaScriptTypeOptions"
                    placeholder="选择javascript类型"
                    show-search
                    style="width: 100px"
                    @change="handleJavascriptTypeChange"
                    @press-enter="save(record.columnId, 'typeScriptType')"
                  />
                  <MaterialCheck
                    @click="save(record.columnId, 'typeScriptType')"
                  />
                </a-space>
              </div>
              <div v-else class="editable-cell-text-wrapper">
                <a-space style="margin-bottom: 10px">
                  {{ text || ' ' }}
                  <MaterialEdit
                    @click="edit(record.columnId, 'typeScriptType')"
                  />
                </a-space>
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'isRequired'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_isRequired`]"
                class="editable-cell-input-wrapper"
              >
                <a-space style="margin-bottom: 10px">
                  <a-checkbox
                    v-model:checked="
                      editableData[`${record.columnId}_isRequired`].isRequired
                    "
                  />
                  <MaterialCheck @click="save(record.columnId, 'isRequired')" />
                </a-space>
              </div>
              <div v-else class="editable-cell-text-wrapper">
                <a-space style="margin-bottom: 10px">
                  {{ text ? '是' : '否' }}
                  <MaterialEdit @click="edit(record.columnId, 'isRequired')" />
                </a-space>
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'htmlType'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_htmlType`]"
                class="editable-cell-input-wrapper"
              >
                <a-space style="margin-bottom: 10px">
                  <a-select
                    v-model:value="
                      editableData[`${record.columnId}_htmlType`].htmlType
                    "
                    :filter-option="filterOption"
                    :options="htmlTypeOptions"
                    placeholder="选择显示类型"
                    show-search
                    style="width: 100px"
                    @change="handleHtmlTypeChange"
                    @press-enter="save(record.columnId, 'htmlType')"
                  />
                  <MaterialCheck @click="save(record.columnId, 'htmlType')" />
                </a-space>
              </div>
              <div v-else class="editable-cell-text-wrapper">
                <a-space style="margin-bottom: 10px">
                  {{ text || ' ' }}
                  <MaterialEdit @click="edit(record.columnId, 'htmlType')" />
                </a-space>
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'dictType'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_dictType`]"
                class="editable-cell-input-wrapper"
              >
                <a-space style="margin-bottom: 10px">
                  <a-input
                    v-model:value="
                      editableData[`${record.columnId}_dictType`].dictType
                    "
                    @press-enter="save(record.columnId, 'dictType')"
                  />
                  <MaterialCheck @click="save(record.columnId, 'dictType')" />
                </a-space>
              </div>
              <div v-else class="editable-cell-text-wrapper">
                <a-space style="margin-bottom: 10px">
                  {{ text || ' ' }}
                  <MaterialEdit @click="edit(record.columnId, 'dictType')" />
                </a-space>
              </div>
            </div>
          </template>
        </template>
      </a-table>
    </a-drawer>
  </div>
</template>
