import {isURL} from "./validate";

export function timeFix() {
    const time = new Date()
    const hour = time.getHours()
    return hour < 9 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour < 20 ? '下午好' : '晚上好')))
}

export function welcome() {
    const arr = ['休息一会儿吧', '准备吃什么呢?', '我猜你可能累了']
    let index = Math.floor((Math.random()*arr.length))
    return arr[index]
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent() {
    let event = document.createEvent('HTMLEvents')
    event.initEvent('resize', true, true)
    event.eventType = 'message'
    window.dispatchEvent(event)
}

/**
 * 生成首页路由
 * @param data
 */
export function generateHomepageRouter(data){
    let homepageRouter = [{
        path:'/',
        name: 'dashboard',
        component: resolve => require(['@/components/layouts/TabLayout'], resolve),
        meta: { title: '首页'},
        redirect: '/dashboard/workspace',
        children: [
            ...generateChildRouters(data)
        ]
    },
        {
        path: '*', redirect: "/404", "hidden": true
        }]
    return homepageRouter
}

/**
 * 生成嵌套路由（子路由）
 * @param data
 */
function generateChildRouters(data){
    const routers = []
    for(let item of data) {
        let component = ""
        if (item.component.indexOf("layouts") >= 0) {
            component = "components/" + item.component
        } else {
            component = "views/" + item.component
        }

        // URL支持{{ window.xxx }}占位符变量
        let URL = (item.meta.url || '').replace(/{{([^}}]+)?}}/g, (s1, s2) => eval(s2))
        if (isURL(URL)) {
            item.meta.url = URL
        }

        let menu = {
            path: item.path,
            name: item.name,
            redirect: item.redirect,
            component: resolve => require(['@/'+component+'.vue'], resolve),
            hidden: item.hidden,
            meta: {
                title: item.meta.title,
                icon: item.meta.icon,
                url: item.meta.url,
                permissionList: item.meta.permissionList,
                keepAlive: item.meta.keepAlive,
                internalOrExternal: item.meta.internalOrExternal
            }
        }

        // 如果菜单聚合显示
        if(item.alwaysShow){
            menu.alwaysShow = true
            menu.redirect = menu.path
        }

        // 递归查找子菜单
        if(item.children && item.children.length > 0){
            menu.children = [...generateChildRouters(item.children)]
        }

        //判断是否生成路由
        if(item.route && item.route === '0'){
            console.log(' 不生成路由 item.route：  '+item.route)
            console.log(' 不生成路由 item.path：  '+item.path)
        }else{
            routers.push(menu)
        }
    }
    return routers
}

/**
 * 过滤对象中为空的属性
 * @param obj
 * @returns {*}
 */
export function filterObj(obj) {
    if (!(typeof obj == 'object')) {
        return;
    }

    for ( var key in obj) {
        if (Object.prototype.hasOwnProperty.call(obj, key)
            && (obj[key] == null || obj[key] == undefined || obj[key] === '')) {
            delete obj[key];
        }
    }
    return obj;
}

/**
 * 获取文件访问路径
 * @param avatar
 * @param imgServer
 * @param str
 * @returns {*}
 */
export function getFileAccessHttpUrl(avatar,imgServer,subStr) {
    if(avatar && avatar.indexOf(subStr) != -1 ){
        return avatar;
    }else{
        return imgServer + "/" + avatar;
    }
}


