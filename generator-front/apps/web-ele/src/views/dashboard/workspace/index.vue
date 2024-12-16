<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { useGeneratorStore } from "#/store";

const getDataList = async () => {
  // console.log("getDataList");

  const generatorStore = useGeneratorStore();

  const responseData = await generatorStore.fetchTableList({
    tableName: state.queryForm.tableName,
  });

  state.dataList = responseData;

  console.log("getDataList:", responseData);
}

const importHandle = () => {

}

const downloadBatchHandle = () => {

}

const deleteBatchHandle = (rowId) => {}

const editHandle = (rowId) => {
  console.log(rowId);
}

const previewHandle = (rowId) => {
  console.log(rowId);
}

const generatorHandle = (rowId) => {
  console.log(rowId);
}

const syncHandle = (rowId) => {
  console.log(rowId);
}

const selectionChangeHandle = () => {

}

type QueryFormType = {
  tableName?: string;
};

type IHooksOptions = {
  dataListUrl?: string
  deleteUrl?: string
  queryForm: QueryFormType
  dataListLoading: false
  dataList: []
  page: number,
  pageSizes: number[]
  limit: number
  total: number
};



const state: IHooksOptions = reactive({
  dataListUrl: '/gen/table/page',
  deleteUrl: '/gen/table',
  queryForm: {
    tableName: ''
  }
})

const sizeChangeHandle = () => {

}

const currentChangeHandle = () => {}


</script>

<template>
  <el-card>
    <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
      <el-form-item>
        <el-input v-model="state.queryForm.tableName" placeholder="表名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="importHandle()">导入</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="downloadBatchHandle()">生成代码</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%" @selection-change="selectionChangeHandle">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="tableName" label="表名" header-align="center" align="center"></el-table-column>
      <el-table-column prop="tableComment" label="表说明" header-align="center" align="center"></el-table-column>
      <el-table-column prop="className" label="类名" header-align="center" align="center"></el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="300">
        <template #default="scope">
          <el-button type="primary" link @click="editHandle(scope.row.id)">编辑</el-button>
          <el-button type="primary" link @click="previewHandle(scope.row.id)">预览</el-button>
          <el-button type="primary" link @click="generatorHandle(scope.row.id)">生成代码</el-button>
          <el-button type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
          <el-button type="primary" link @click="syncHandle(scope.row)">同步</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="state.page"
      :page-sizes="state.pageSizes"
      :page-size="state.limit"
      :total="state.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
    >
    </el-pagination>

    <import ref="importRef" @refresh-data-list="getDataList"></import>
    <preview ref="previewRef" @refresh-data-list="getDataList"></preview>
    <edit ref="editRef" @refresh-data-list="getDataList"></edit>
    <generator ref="generatorRef" @refresh-data-list="getDataList"></generator>
  </el-card>
</template>
