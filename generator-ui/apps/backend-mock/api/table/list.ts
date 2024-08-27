import { verifyAccessToken } from '~/utils/jwt-utils';
import { MOCK_TABLES } from '~/utils/mock-data';
import { unAuthorizedResponse } from '~/utils/response';

export default eventHandler((event) => {
  const userinfo = verifyAccessToken(event);
  if (!userinfo) {
    return unAuthorizedResponse(event);
  }

  const menus = { results: MOCK_TABLES[0].tabels };
  return useResponseSuccess(menus);
});
