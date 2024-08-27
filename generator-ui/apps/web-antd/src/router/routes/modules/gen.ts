import type { RouteRecordRaw } from 'vue-router';

import { BasicLayout } from '#/layouts';
import { $t } from '#/locales';

const routes: RouteRecordRaw[] = [
  {
    component: BasicLayout,
    meta: {
      icon: 'ic:baseline-view-in-ar',
      keepAlive: true,
      order: 1000,
      title: $t('page.gen.title'),
    },
    name: 'Gen',
    path: '/gen',
    children: [
      {
        meta: {
          title: $t('page.gen.code'),
        },
        name: 'GeneratorCode',
        path: '/gen/code',
        component: () => import('#/views/gen/code/index.vue'),
      },
      {
        meta: {
          title: $t('page.gen.datasource'),
        },
        name: 'GeneratorDatasource',
        path: '/gen/datasource',
        component: () => import('#/views/gen/datasource/index.vue'),
      },
    ],
  },
];

export default routes;
