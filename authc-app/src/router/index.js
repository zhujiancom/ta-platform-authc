import Vue from 'vue'
import Router from 'vue-router'
import {constantRouterMap} from "./config";

// 解决vue-router 3.1 之后版本浏览器出现的NavigationDuplicated错误问题
const originalPush = Router.prototype.push
Router.prototype.push = function push(location, onResolve, onReject) {
    if(onResolve || onReject){
        return originalPush.call(this, location, onResolve, onReject)
    }
    return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    scrollBehavior: () => ({y: 0}),
    routes: constantRouterMap
})
