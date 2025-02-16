<script lang="ts" setup>
import { reactive, ref } from 'vue';

/* import {
  useDataSourceListApi,
  useDataSourceTableListApi,
} from '@/api/datasource';
import { useTableImportSubmitApi } from '@/api/table';*/
import { ElMessage } from 'element-plus/es';

// const emit = defineEmits(['refreshDataList']);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  id: '',
  tableNameListSelections: [] as any,
  datasourceId: '',
  datasourceList: [] as any,
  tableList: [] as any,
  table: {
    tableName: '',
  },
});

// 多选
const selectionChangeHandle = (selections: any[]) => {
  dataForm.tableNameListSelections = selections.map(
    (item: any) => item.tableName,
  );
};

const getDataSourceList = () => {
  /* useDataSourceListApi().then((res) => {
    dataForm.datasourceList = res.data;
  });*/
};

const init = (id?: number) => {
  visible.value = true;
  dataForm.id = String(id);

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  dataForm.tableList = [];

  getDataSourceList();
};

const getTableList = () => {
  dataForm.table.tableName = '';
  /* useDataSourceTableListApi(dataForm.datasourceId).then((res) => {
    dataForm.tableList = res.data;
  });*/
};

// 表单提交
const submitHandle = () => {
  const tableNameList = dataForm?.tableNameListSelections ?? [];
  if (tableNameList.length === 0) {
    ElMessage.warning('请选择记录');
  }

  /* useTableImportSubmitApi(dataForm.datasourceId, tableNameList).then(() => {
    ElMessage.success({
      message: '操作成功',
      duration: 500,
      onClose: () => {
        visible.value = false;
        emit('refreshDataList');
      },
    });
  });*/
};

const testChild = () => {
  console.log('testChild');
};

defineExpose({
  testChild,
  init,
});
</script>

<template>
  <el-dialog
    v-model="visible"
    :close-on-click-modal="false"
    draggable
    title="导入数据库表"
  >
    <el-form ref="dataFormRef" :model="dataForm">
      <el-form-item label="数据源" prop="datasourceId">
        <el-select
          v-model="dataForm.datasourceId"
          placeholder="请选择数据源"
          style="width: 100%"
          @change="getTableList"
        >
          <el-option label="默认数据源" value="0" />
          <el-option
            v-for="ds in dataForm.datasourceList"
            :key="ds.id"
            :label="ds.connName"
            :value="ds.id"
          />
        </el-select>
      </el-form-item>
      <el-table
        :data="dataForm.tableList"
        :max-height="400"
        border
        style="width: 100%"
        @selection-change="selectionChangeHandle"
      >
        <el-table-column
          align="center"
          header-align="center"
          type="selection"
          width="60"
        />
        <el-table-column
          align="center"
          header-align="center"
          label="表名"
          prop="tableName"
        />
        <el-table-column
          align="center"
          header-align="center"
          label="表说明"
          prop="tableComment"
        />
      </el-table>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>
