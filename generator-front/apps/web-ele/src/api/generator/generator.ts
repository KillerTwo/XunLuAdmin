import { baseRequestClient, requestClient } from '#/api/request';


export namespace generator {

  export interface GenerateRequestParams {}

  export interface TableBody {
    tableId: number;
    tableName: string;
    tableComment?: string;
    className?: string;
    packageName?: string;
    moduleName?: string;
    businessName?: string;
    functionName?: string;
    functionAuthor?: string;
    genPath?: string;
  }
}


/**
 * 获取表list
 */
export async function getTableListApi(tabelName?: string) {
  return requestClient.get<generator.TableBody[]>(
    `/generator/list?tableName=${tabelName}`,
  );
}
