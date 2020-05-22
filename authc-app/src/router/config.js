/**
 * 动态路由， 走菜单、权限控制
 * @type {*[]}
 */
import {UserLayout, TabLayout} from "@layout";

export const asyncRouterMap = [
    {
        path: '/',
        name: 'dashboard',
        component: TabLayout,
        meta: { title: '首页' },
        redirect: '/dashboard/workplace',
        children: []
    },
    {path: '*', redirect: '/404', hidden: true}
]

/**
 * 基础路由
 * @type {*[]}
 */
export const constantRouterMap = [
    // 通过路由守卫动态生成首页路由 util.js generateHomepageRouter
    // {
    //     path: '/',
    //     name: 'dashboard',
    //     component: TabLayout,
    //     meta: { title: '首页' }
    // },
    {
        path: '/user',
        component: UserLayout,
        redirect: '/user/login',
        hidden: true,
        children: [
            {
                path: 'login',
                name: 'login',
                component: () => import('@views/user/Login')
            },
            {
                path: 'register',
                name: 'register',
                component: () => import('@/views/user/Register')
            },
            {
                path: 'register-result',
                name: 'registerResult',
                component: () => import('@/views/user/RegisterResult')
            },
        ]
    },
    {
        path: '/404',
        component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
    },
]
