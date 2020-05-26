import Vue from 'vue'

import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'
import {ACCESS_TOKEN} from "./store/mutation-types";
import {generateHomepageRouter} from "./utils/util";
import {notification} from "ant-design-vue"; // progress bar style

// 路由白名单， 访问这些路径时， 不会被路由拦截
const whiteList = ['/user/login', '/user/register', '/user/register-result']

router.beforeEach((to, from, next) => {
    NProgress.start()

    if(Vue.ls.get(ACCESS_TOKEN)){
        // 如果用户已登录， 并且跳转的目标路由是登录界面的话， 自动跳转到个人工作台界面
        if(to.path === '/user/login'){
            next({path: '/dashboard/workspace'})
            NProgress.done()
        } else{
            if(store.getters.permissionList.length === 0){
                // 如果登录用户还没有权限列表， 则去后台获取权限列表信息
                store.dispatch('GetPermissionList').then(res => {
                    const menuData = res.data.menu
                    // console.log(res.message)
                    if(menuData === null || menuData === "" || menuData === undefined){
                        return;
                    }
                    let constRoutes = [];
                    constRoutes = generateHomepageRouter(menuData);
                    // 添加主界面路由, 调用 module/permission.js 中的action方法
                    store.dispatch('UpdateAppRouter', {constRoutes}).then(() => {
                        // 根据roles 权限生成可访问的路由表
                        // 动态添加可访问路由表
                        router.addRoutes(store.getters.addRouters)
                        // console.dir(router)
                        const redirect = decodeURIComponent(from.query.redirect || to.path)
                        if(to.path === redirect){
                            // hack 方法， 确保addRoutes已完成，
                            next({...to, replace: true})
                        }else{
                            // 跳转到目标路由
                            next({path: redirect})
                        }
                    })
                }).catch((err)=>{
                        console.log(err)
                        notification.error({
                            message: '系统提示',
                            description: '请求用户信息失败， 请重试！'
                        })
                        store.dispatch('Logout').then(()=>{
                            next({path:'user/login',query: {redirect: to.fullPath}})
                        })
                    })
            }else{
                next()
            }
        }
    }else{
        if(whiteList.indexOf(to.path) !== -1){
            // 在白名单中， 直接进入
            next()
        }else{
            next({path: '/user/login', query:{redirect : to.fullPath}})
            NProgress.done()
        }
    }
})

router.afterEach(() => {
    NProgress.done() // finish progress bar
})
