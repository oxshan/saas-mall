import { defineConfig } from '@umijs/max';

export default defineConfig({
  antd: {
    theme: {
      token: {
        colorPrimary: '#1890ff',
      },
    },
  },
  access: {},
  model: {},
  initialState: {},
  request: {},
  layout: {
    title: '商家后台',
    locale: false,
    layout: 'mix',
    fixedHeader: true,
    splitMenus: false,
    token: {
      sider: {
        colorMenuBackground: '#e6f4ff',
        colorTextMenu: '#1890ff',
        colorTextMenuSelected: '#fff',
        colorBgMenuItemSelected: '#1890ff',
      },
    },
  },
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      pathRewrite: { '^/api': '' },
    },
  },
  routes: [
    {
      path: '/login',
      layout: false,
      component: './Login',
    },
    {
      path: '/',
      redirect: '/dashboard',
    },
    {
      path: '/dashboard',
      name: '工作台',
      icon: 'dashboard',
      component: './Dashboard',
    },
    {
      path: '/system',
      name: '系统管理',
      icon: 'setting',
      routes: [
        { path: '/system/user', name: '用户管理', component: './System/User' },
        { path: '/system/role', name: '角色管理', component: './System/Role' },
        { path: '/system/menu', name: '菜单管理', component: './System/Menu' },
      ],
    },
  ],
  npmClient: 'yarn',
});
