import { definePlugin } from '@halo-dev/ui-shared'
import { markRaw } from 'vue'
import RiFolderLine from '~icons/ri/folder-line'
import RiSettings3Line from '~icons/ri/settings-3-line'
import RiArticleLine from '~icons/ri/article-line'

export default definePlugin({
  components: {},
  routes: [
    {
      parentName: 'Root',
      route: {
        path: '/journal/categories',
        name: 'JournalCategories',
        component: () => import('./views/CategoryView.vue'),
        meta: {
          title: '期刊类别',
          searchable: true,
          menu: {
            name: '期刊类别',
            group: '期刊模块',
            icon: markRaw(RiFolderLine),
            priority: 10,
          },
        },
      },
    },
    {
      parentName: 'Root',
      route: {
        path: '/journal/management',
        name: 'JournalManagement',
        component: () => import('./views/ManagementView.vue'),
        meta: {
          title: '期刊管理',
          searchable: true,
          menu: {
            name: '期刊管理',
            group: '期刊模块',
            icon: markRaw(RiSettings3Line),
            priority: 20,
          },
        },
      },
    },
    {
      parentName: 'Root',
      route: {
        path: '/journal/articles',
        name: 'JournalArticles',
        component: () => import('./views/ArticleView.vue'),
        meta: {
          title: '期刊文章',
          searchable: true,
          menu: {
            name: '期刊文章',
            group: '期刊模块',
            icon: markRaw(RiArticleLine),
            priority: 30,
          },
        },
      },
    },
  ],
  extensionPoints: {},
})
