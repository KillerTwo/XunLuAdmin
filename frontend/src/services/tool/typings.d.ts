declare namespace Tool {
  type PageParams = {
    current?: number | 0;
    pageSize?: number | 10;
  };

  type ResponseResult = {
    msg: string;
    code: number;
    data?: any;
    total?: number;
  };

  type GenTable = {
    tableId?: number;
    tableName?: string;
    tableComment?: string;
    subTableName?: string;
    subTableFkName?: string;
    className?: string;
    tplCategory?: string;
    packageName?: string;
    moduleName?: string;
    businessName?: string;
    functionName?: string;
    functionAuthor?: string;
    genType?: string;
    genPath?: string;
    options?: string;
    treeCode?: string;
    treeParentCode?: string;
    treeName?: string;
    parentMenuId?: string;
    parentMenuName?: string;
    remark?: string;
  };

  type GenTableColumn = {
    columnId?: number;
    tableId?: number;
    columnName?: string;
    columnComment?: string;
    columnType?: string;
    javaType?: string;
    javaField?: string;
    isPk?: string;
    isIncrement?: string;
    isRequired?: string;
    isInsert?: string;
    isEdit?: string;
    isList?: string;
    isQuery?: string;
    queryType?: string;
    htmlType?: string;
    dictType?: string;
    sort?: number;
    typeScriptType?: string;
  };
}
