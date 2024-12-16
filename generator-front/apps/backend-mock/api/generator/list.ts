import { verifyAccessToken } from '~/utils/jwt-utils';
import { unAuthorizedResponse } from '~/utils/response';
import { MOCK_TABLES } from "~/utils/mock-data";

export default eventHandler((event) => {
  const userinfo = verifyAccessToken(event);
  if (!userinfo) {
    return unAuthorizedResponse(event);
  }

  const { page, pageSize, tableName } = getQuery(event);

  const tables =
    MOCK_TABLES.filter((item) => item.tableName === tableName || tableName === '') ?? [];

  return useResponseSuccess(tables);
});
