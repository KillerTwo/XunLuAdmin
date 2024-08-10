<template>
  <Card>

    <a-space>
      <a-input v-model:value="searchValue" placeholder="查询" style="width: 300px" />
      <a-button>查询</a-button>

      <a-button class="editable-add-btn" style="margin-bottom: 8px;margin-left: 5px" >导入</a-button>
      <a-button class="editable-add-btn" style="margin-bottom: 8px;margin-left: 5px" >生成代码</a-button>
      <a-button class="editable-add-btn" style="margin-bottom: 8px;margin-left: 5px" >删除</a-button>
    </a-space>


    <Table :columns="columns" :data-source="dataSource" bordered>
      <template #headerCell="{ column }">
        <template v-if="['tableId', 'tableName','tableComment','className'].indexOf(column.key) !== -1">
          <span>
            {{ column.title }}
          </span>
        </template>
      </template>

      <template #bodyCell="{ column, record }">
        <template v-if="'tableId' === column.key">
          <a>
            {{ record.tableId }}
          </a>
        </template>
        <template v-else-if="'tableName' === column.key">
          <a>
            {{ record.tableName }}
          </a>
        </template>
        <template v-else-if="'tableComment' === column.key">
          <a>
            {{ record.tableComment }}
          </a>
        </template>
        <template v-else-if="column.key === 'operation'">
          <span>
            <a>编辑</a>
            <a>预览</a>
            <a>生成代码</a>
            <a>删除</a>
            <a>同步</a>
          </span>
        </template>
      </template>
    </Table>
  </Card>
</template>
<script lang="ts" setup>
import { cloneDeep } from 'lodash-es';
import { reactive, ref, watch } from 'vue';
import type { UnwrapRef } from 'vue';
import { Table, Card } from "ant-design-vue";

const searchValue = ref<string>('');


const columns = [
  {
    title: '序号',
    dataIndex: 'tableId',
    key: 'tableId',
    width: '10%',
  },
  {
    title: '表名称',
    dataIndex: 'tableName',
    key: 'tableName',
    width: '15%',
  },
  {
    title: '表描述',
    dataIndex: 'tableComment',
    key: 'tableComment',
    width: '40%',
  },
  {
    title: '实体',
    dataIndex: 'className',
    key: 'className',
    width: '20%',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    width: '15%',
  },
];
interface DataItem {
  tableId?: string
  tableName?: string
  tableComment?: string
  className?: string
}
const data: DataItem[] = [];
for (let i = 0; i < 100; i++) {
  const dataItem: DataItem = {
    tableId: i.toString(),
    tableName: `Edrward ${i}`,
    tableComment: `London Park no. ${i}`,
    className: `Clazz. ${i}`,
  };
  data.push(dataItem);
}

const dataSource = ref(data);
const editableData: UnwrapRef<Record<string, DataItem>> = reactive({});

/*const edit = (key: string) => {
  editableData[key] = cloneDeep(dataSource.value.filter(item => key === item.key)[0]);
};
const save = (key: string) => {
  Object.assign(dataSource.value.filter(item => key === item.key)[0], editableData[key]);
  delete editableData[key];
};*/
const cancel = (key: string) => {
  delete editableData[key];
};
</script>
<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
</style>
