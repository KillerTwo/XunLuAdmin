<script lang="ts" setup>
// import { SmileOutlined, DownOutlined } from '@vben/icons';
import type { TableProps } from 'ant-design-vue';

import {computed, reactive, ref, watch, watchEffect} from 'vue';

// import {Table, Button} from "ant-design-vue";
import { Page } from '@vben/common-ui';

import { requestClient } from '#/api/request';

const tableName = ref<string>('');

const current = ref<number>(1);
const pageSize = ref<number>(10);
const datasource = ref<any[]>([]);
const loading = ref<boolean>(false);

watch(tableName, () => {
  // console.log(tableName.value);
});

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
  console.log('selectedRowKeys changed:', selectedRowKeys);
  state.selectedRowKeys = selectedRowKeys;
};

</script>

<template>
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
            <a-button>编辑</a-button>
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
</template>
