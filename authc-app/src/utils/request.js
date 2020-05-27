import Vue from 'vue'
import axios from 'axios'
import store from '@/store'
import {ACCESS_TOKEN} from "@/store/mutation-types";
import {Modal, notification} from "ant-design-vue";
import {VueAxios} from "./axios";


axios.defaults.withCredentials = true
//自动设置后台服务 baseURL (也可以手工指定写死项目名字)
let baseDomain = window._CONFIG['domainURL'];
let baseProject = baseDomain.substring(baseDomain.lastIndexOf("/"));
console.log("baseDomain= ",baseDomain)
console.log("baseProject= ",baseProject)

// 创建 axios 实例
const service = axios.create({
    baseURL: '/api', // 与 http-proxy-middleware 一起使用解决跨域问题 , baseURL 不能使用 / ,否则如果直接在浏览器地址栏中输入路由地址，请求头中不会带access-token
    timeout: 9000 //请求超时时间
})

const err = (error) => {
    if(error.response){
        let data = error.response.data
        const token = Vue.ls.get(ACCESS_TOKEN)
        console.log("---------异常响应---------", token);
        console.log("---------异常响应---------",error.response)
        switch (error.response.status) {
            case 403:
                notification.error({message: '系统提示', description: '拒绝访问', duration: 4})
                break
            case 500:
                //TODO response返回的消息还没有code
                if(token && data.code === process.env.VUE_APP_TOKEN_INVALID){
                    Modal.error({
                        title: '登录已过期',
                        content: '很抱歉，登录已过期，请重新登录',
                        okText: '重新登录',
                        mask:false,
                        onOk: () => {
                            store.dispatch('Logout').then(() => {
                                Vue.ls.remove(ACCESS_TOKEN)
                                window.location.reload()
                            })
                        }
                    })
                }else{
                    notification.error({message: '系统提示', description: error.response.statusText, duration: 5})
                }
                break
            case 404:
                notification.error({message: '系统提示', description: '很抱歉， 资源未找到！', duration: 4})
                break
            case 504:
                notification.error({message: '系统提示', description: '网络超时'})
                break
            case 401:
                notification.error({message: '系统提示', description: '未授权， 请重新登录', duration:4})
                if(token){
                    store.dispatch('Logout').then(() => {
                        setTimeout(() => {
                            window.location.reload();
                        }, 1500)
                    })
                }
                break
            default:
                notification.error({message: '系统提示', description: data.message, duration:4})
                break
        }
    }else{
        // 处理断网情况
        // 请求超时或断网时， 更新 state的network状态
        // network状态在app.vue中控制这一个全局的断网提示组件的显示和隐藏
        if(!window.navigator.onLine){
            store.commit('changeNetwork', true)
        }
    }
    return Promise.reject(error)
};

// request 请求拦截器
service.interceptors.request.use(config => {
    const token = Vue.ls.get(ACCESS_TOKEN)
    if(token){
        config.headers[ 'X-Access-Token' ] = token // 让每个请求携带后台返回的自定义的token
    }
    if(config.method=='get'){
        if(config.url.indexOf("sys/dict/getDictItems") < 0){
            config.params = {
                _t: Date.parse(new Date())/1000,
                ...config.params
            }
        }
    }
    return config
}, (error) => {
    return Promise.reject(error)
})

service.interceptors.response.use((response) => {
    // console.log("response = ", response)
    // console.log("response.headers = ", response.headers)
    return response.data
}, err)

/**
 * axios并不像 vue-resource 一样拥有install，即不能直接 Vue.use(axios) 来使用，所以需要我们自己根据axios来写一个具有install方法的Http库
 * 在main.js 中注入 Vue.use(VueAxios, router)
 * @type {{install(*, *=): void, vm: {}}}
 */
const installer = {
    vm: {},
    install(Vue, router={}){
        Vue.use(VueAxios, router, service)
    }
}

export{
    installer as VueAxios,
    service as axios
}
