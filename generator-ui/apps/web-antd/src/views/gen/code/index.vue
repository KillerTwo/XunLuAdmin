<script lang="ts" setup>
// import { SmileOutlined, DownOutlined } from '@vben/icons';
import type { DrawerProps, TableProps } from 'ant-design-vue';

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
  isRequired: string;
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
    title: '编号',
  },
  {
    dataIndex: 'columnName',
    key: 'columnName',
    title: '字段名',
  },
  {
    dataIndex: 'columnComment',
    key: 'columnComment',
    title: '字段说明',
  },
  {
    dataIndex: 'columnType',
    key: 'columnType',
    title: '字段类型',
  },
  {
    dataIndex: 'javaField',
    key: 'javaField',
    title: '属性名',
  },
  {
    dataIndex: 'javaType',
    key: 'javaType',
    title: '属性类型',
  },
  {
    dataIndex: 'typeScriptType',
    key: 'typeScriptType',
    title: 'TypeScript类型',
  },
  {
    dataIndex: 'isPk',
    key: 'isPk',
    title: '是否主键',
  },
  {
    dataIndex: 'isIncrement',
    key: 'isIncrement',
    title: '是否自增',
  },
  {
    dataIndex: 'isRequired',
    key: 'isRequired',
    title: '是否必填',
  },
  {
    dataIndex: 'htmlType',
    key: 'htmlType',
    title: '显示类型',
  },
  {
    dataIndex: 'dictType',
    key: 'dictType',
    title: '字典类型',
  },
  {
    dataIndex: 'sort',
    key: 'sort',
    title: '排序',
  },
];

const dataSource: Ref<ColumnType[]> = ref([
  {
    columnComment: 'London',
    columnId: 1,
    columnName: 'Edward King 0',
    columnType: 'London, Park Lane no. 0',
    dictType: 'London, Park Lane no. 0',
    htmlType: 'London, Park Lane no. 0',
    isIncrement: 'London, Park Lane no. 0',
    isPk: 'London, Park Lane no. 0',
    isRequired: 'London, Park Lane no. 0',
    javaField: 'London, Park Lane no. 0',
    javaType: 'London, Park Lane no. 0',
    sort: 1,
    typeScriptType: 'London, Park Lane no. 0',
  },
  {
    columnComment: 'London',
    columnId: 2,
    columnName: 'Edward King 0',
    columnType: 'London, Park Lane no. 0',
    dictType: 'London, Park Lane no. 0',
    htmlType: 'London, Park Lane no. 0',
    isIncrement: 'London, Park Lane no. 0',
    isPk: 'London, Park Lane no. 0',
    isRequired: 'London, Park Lane no. 0',
    javaField: 'London, Park Lane no. 0',
    javaType: 'London, Park Lane no. 0',
    sort: 2,
    typeScriptType: 'London, Park Lane no. 0',
  },
  {
    columnComment: 'London',
    columnId: 3,
    columnName: 'Edward King 0',
    columnType: 'London, Park Lane no. 0',
    dictType: 'London, Park Lane no. 0',
    htmlType: 'London, Park Lane no. 0',
    isIncrement: 'London, Park Lane no. 0',
    isPk: 'London, Park Lane no. 0',
    isRequired: 'London, Park Lane no. 0',
    javaField: 'London, Park Lane no. 0',
    javaType: 'London, Park Lane no. 0',
    sort: 3,
    typeScriptType: 'London, Park Lane no. 0',
  },
  {
    columnComment: 'London',
    columnId: 4,
    columnName: 'Edward King 0',
    columnType: 'London, Park Lane no. 0',
    dictType: 'London, Park Lane no. 0',
    htmlType: 'London, Park Lane no. 0',
    isIncrement: 'London, Park Lane no. 0',
    isPk: 'London, Park Lane no. 0',
    isRequired: 'London, Park Lane no. 0',
    javaField: 'London, Park Lane no. 0',
    javaType: 'London, Park Lane no. 0',
    sort: 4,
    typeScriptType: 'London, Park Lane no. 0',
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
const javaTypeOptions = [
  { Integer: 'Integer' },
  { Float: 'Float' },
  { Long: 'Long' },
  { Boolean: 'Boolean' },
  { String: 'String' },
  { Double: 'Double' },
  { Date: 'Date' },
  { LocalDate: 'LocalDate' },
  { LocalTime: 'LocalTime' },
  { LocalDateTime: 'LocalDateTime' },
  { Object: 'Object' },
];
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
      <a-table :columns="editColumns" :data-source="dataSource" bordered>
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.key === 'columnComment'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_columnComment`]"
                class="editable-cell-input-wrapper"
              >
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
              </div>
              <div v-else class="editable-cell-text-wrapper">
                {{ text || ' ' }}
                <MaterialEdit @click="edit(record.columnId, 'columnComment')" />
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'javaField'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_javaField`]"
                class="editable-cell-input-wrapper"
              >
                <a-input
                  v-model:value="
                    editableData[`${record.columnId}_javaField`].javaField
                  "
                  @press-enter="save(record.columnId, 'javaField')"
                />
                <MaterialCheck @click="save(record.columnId, 'javaField')" />
              </div>
              <div v-else class="editable-cell-text-wrapper">
                {{ text || ' ' }}
                <MaterialEdit @click="edit(record.columnId, 'javaField')" />
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'javaType'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_javaType`]"
                class="editable-cell-input-wrapper"
              >
                <a-select
                  v-model:value="
                    editableData[`${record.columnId}_javaType`].javaType
                  "
                  :options="javaTypeOptions"
                  mode="tags"
                  placeholder="Java属性类型"
                  style="width: 100%"
                  @change="handleJavaTypeChange"
                  @press-enter="save(record.columnId, 'javaType')"
                />
                <MaterialCheck @click="save(record.columnId, 'javaType')" />
              </div>
              <div v-else class="editable-cell-text-wrapper">
                {{ text || ' ' }}
                <MaterialEdit @click="edit(record.columnId, 'javaType')" />
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'typeScriptType'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_typeScriptType`]"
                class="editable-cell-input-wrapper"
              >
                <a-input
                  v-model:value="
                    editableData[`${record.columnId}_typeScriptType`]
                      .typeScriptType
                  "
                  @press-enter="save(record.columnId, 'typeScriptType')"
                />
                <MaterialCheck
                  @click="save(record.columnId, 'typeScriptType')"
                />
              </div>
              <div v-else class="editable-cell-text-wrapper">
                {{ text || ' ' }}
                <MaterialEdit
                  @click="edit(record.columnId, 'typeScriptType')"
                />
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'htmlType'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_htmlType`]"
                class="editable-cell-input-wrapper"
              >
                <a-input
                  v-model:value="
                    editableData[`${record.columnId}_htmlType`].htmlType
                  "
                  @press-enter="save(record.columnId, 'htmlType')"
                />
                <MaterialCheck @click="save(record.columnId, 'htmlType')" />
              </div>
              <div v-else class="editable-cell-text-wrapper">
                {{ text || ' ' }}
                <MaterialEdit @click="edit(record.columnId, 'htmlType')" />
              </div>
            </div>
          </template>
          <template v-else-if="column.key === 'dictType'">
            <div class="editable-cell">
              <div
                v-if="editableData[`${record.columnId}_dictType`]"
                class="editable-cell-input-wrapper"
              >
                <a-input
                  v-model:value="
                    editableData[`${record.columnId}_dictType`].dictType
                  "
                  @press-enter="save(record.columnId, 'dictType')"
                />
                <MaterialCheck @click="save(record.columnId, 'dictType')" />
              </div>
              <div v-else class="editable-cell-text-wrapper">
                {{ text || ' ' }}
                <MaterialEdit @click="edit(record.columnId, 'dictType')" />
              </div>
            </div>
          </template>
        </template>
      </a-table>
    </a-drawer>
  </div>
</template>
