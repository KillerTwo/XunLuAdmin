import { AppRouteModule } from '/@/router/types'
import { LAYOUT } from '/@/router/constant'
import { t } from '/@/hooks/web/useI18n'

const article: AppRouteModule = {
  path: '/article',
  name: 'Article',
  component: LAYOUT,
  redirect: '/article/wait',
  meta: {
    orderNo: 10,
    icon: 'ion:grid-outline',
    title: t('routes.article.article'),
  },
  children: [
    {
      path: 'wait',
      name: 'ArticleWaitPage',
      component: () => import('/@/views/article/wait/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.article.wait'),
      },
    },
    {
      path: 'complete',
      name: 'ArticleCompletePage',
      component: () => import('/@/views/article/complete/index.vue'),
      meta: {
        title: t('routes.article.complete'),
      },
    },
  ],
}

export default article
