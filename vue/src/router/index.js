import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
export const constantRouterMap = [
  {path: '/login', component: _import('login/index'), hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '首页',
    hidden: true,
    children: [{
      path: 'dashboard', component: _import('dashboard/index')
    }]
  }
]
export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export const asyncRouterMap = [
  {
    path: '/system',
    component: Layout,
    redirect: '/system/article',
    name: '功能模块',
    meta: {title: '功能模块', icon: 'tree'},
    children: [
      {
        path: 'article',
        name: '文章',
        component: _import('article/article'),
        meta: {title: '文章', icon: 'example'},
        menu: 'article'
      },
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/',
    name: '',
    meta: {title: '用户权限', icon: 'table'},
    children: [
      {
        path: '',
        name: '用户列表',
        component: _import('user/user'),
        meta: {title: '用户列表', icon: 'user'},
        menu: 'user'
      },
      {
        path: 'role',
        name: '权限管理',
        component: _import('user/role'),
        meta: {title: '权限管理', icon: 'password'},
        menu: 'role'
      },
    ]
  },


  {
    path: '/euser',
    component: Layout,
    redirect: '/euser/',
    name: '',
    meta: {title: '外部用户管理', icon: 'table'},
    children: [
      {
        path: 'euser',
        name: '用户列表',
        component: _import('euser/euser'),
        meta: {title: '用户列表', icon: 'user'},
        menu: 'euser'
      },
      {
        path: 'permission',
        name: '权限管理',
        component: _import('euser/permission'),
        meta: {title: '权限管理', icon: 'password'},
        menu: 'euser'
      },
      {
        path: 'record',
        name: '浏览记录',
        component: _import('euser/record'),
        meta: {title: '浏览记录', icon: 'record'},
        menu: 'euser'
      },
    ]
  },

  {
    path: '/post',
    component: Layout,
    redirect: '/post/',
    name: '',
    meta: {title: '帖子列表', icon: 'post'},
    children: [
      {
        path: '',
        name: '帖子列表',
        component: _import('post/post'),
        meta: {title: '帖子管理', icon: 'post'},
        menu: 'post'
      }
    ]
  },
  {
    path: '/platform',
    component: Layout,
    redirect: '/platform/',
    name: '',
    meta: {title: '平台管理', icon: 'table'},
    children: [
      {
        path: '', name: '帖子类别管理', component: _import('platform/sort'), meta: {title: '帖子类别管理', icon: 'cate'}, menu: 'post'
      },
      {
        path: 'tag',
        name: '帖子标签管理',
        component: _import('platform/tag'),
        meta: {title: '帖子标签管理', icon: 'tag'},
        menu: 'post'
      },
    ]
  },
  {
    path: '/template',
    component: Layout,
    redirect: '/template/',
    name: '',
    meta: {title: '系统信息管理', icon: 'table'},
    children: [
      {
      path: '', name: '消息模块管理', component: _import('template/msgtemplate'), meta: {title: '消息模块管理', icon: 'message'}, menu: 'message'
},

    ]
  },

  {
    path: '/comment',
    component: Layout,
    redirect: '/comment/',
    name: '',
    meta: {title: '评论管理', icon: 'comment'},
    children: [
      {
        path: '',
        name: '评论列表',
        component: _import('comment/comments'),
        meta: {title: '评论管理', icon: 'comment'},
        menu: 'comment'
      }
    ]
  },

  {
    path: '/advertisement',
    component: Layout,
    redirect: '/advertisement/',
    name: '',
    meta: {title: '广告管理', icon: 'advert'},
    children: [
      {
        path: '',
        name: '广告列表',
        component: _import('advertisement/upload'),
        meta: {title: '广告管理', icon: 'advert'},
        menu: 'advert'
      }
    ]
  },

  {path: '*', redirect: '/404', hidden: true}
]
