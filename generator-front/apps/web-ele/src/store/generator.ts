import type { Recordable } from '@vben/types';

import { ref } from 'vue';


import { defineStore } from 'pinia';

import { getTableListApi } from '#/api';


export const useGeneratorStore = defineStore('generator', () => {

  const tableListLoading = ref(false);

  async function fetchTableList(params: Recordable<any>) {

    tableListLoading.value = true;
    try {
      return await getTableListApi(params.tableName);
    } finally {
      tableListLoading.value = false;
    }
  }

  function $reset() {
    tableListLoading.value = false;
  }

  return {
    $reset,
    fetchTableList,
    tableListLoading,
  };
});
