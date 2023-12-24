export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        path: '/user',
        routes: [
          { name: '登录', path: '/user/login', component: './user/Login' },
          { path: '/user/resetPassword', component: './user/ResetPassword' },
        ],
      },
      { component: './404' },
    ],
  },
  {
    path: '/account',
    routes: [
      { path: '/', redirect: '/account/center' },
      { path: '/account/center', component: './account/center' },
      { path: '/account/settings', component: './account/settings' },
    ],
  },
  { path: '/welcome', icon: 'smile', component: './Welcome' },
  {
    path: '/system',
    routes: [
      { name: '用户管理', path: '/system/user', component: './system/UserList' },
      { path: '/system/role', component: './system/RoleList' },
      { path: '/system/menu', component: './system/MenuList' },
      { path: '/system/dept', component: './system/DeptList' },
      { path: '/system/post', component: './system/PostList' },
      { path: '/system/dict', component: './system/DictList' },
      { path: '/system/dictData', component: './system/DictDataList' },
      { path: '/system/config', component: './system/ConfigList' },
      { path: '/system/notice', component: './system/NoticeList' },
      {
        path: '/system/log',
        routes: [
          { path: '/system/log/operlog', component: './system/LoggingList/OperateLogList' },
          { path: '/system/log/logininfor', component: './system/LoggingList/LoginLogList' },
        ],
      },
      { path: '/system/sysJob', component: './system/Jobs' },
      { path: '/system/sysJobLog', component: './system/JobLogs' },
    ],
  },
  {
    path: '/tool',
    routes: [
      { path: '/tool/gen', component: './tool/Generator' },
      { path: '/tool/swagger', component: './tool/SpringDoc' },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { component: './404' },
];
