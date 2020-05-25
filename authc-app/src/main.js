import Vue from 'vue'
import App from './App.vue'

import Antd from 'ant-design-vue'
import {VueAxios} from "@/utils/request"
import Storage from 'vue-ls'
import config from '@/defaultSettings'
import router from './router'
import store from './store/'
// import api from './api'
import 'ant-design-vue/dist/antd.min.css';
import '@/authguard'
import '@/utils/filter' // base filter

import {
    ACCESS_TOKEN,
    SIDEBAR_TYPE,
    DEFAULT_FIXED_HEADER
} from "@/store/mutation-types"
import {
    DEFAULT_COLOR,
    DEFAULT_CONTENT_WIDTH_TYPE,
    DEFAULT_FIXED_HEADER_HIDDEN,
    DEFAULT_FIXED_SIDEMENU,
    DEFAULT_LAYOUT_MODE,
    DEFAULT_MULTI_PAGE,
    DEFAULT_THEME
} from "./store/mutation-types";
import hasPermission from "./directive/hasPermission";

Vue.config.productionTip = false

// vue-ls options
const storageOptions = {
    namespace: 'pro__', // key prefix
    name: 'ls', // name variable Vue.[ls] or this.[$ls],
    storage: 'local', // storage name session, local, memory
}

Vue.use(Antd)
Vue.use(VueAxios, router)
Vue.use(Storage, storageOptions)
Vue.use(hasPermission)

//Vue.prototype.$api = api; // 将api挂载到vue的原型上

new Vue({
    router,
    store,
    mounted() {
        store.commit('SET_SIDEBAR_TYPE', Vue.ls.get(SIDEBAR_TYPE, true))
        store.commit('TOGGLE_THEME', Vue.ls.get(DEFAULT_THEME, config.navTheme))
        store.commit('TOGGLE_LAYOUT_MODE', Vue.ls.get(DEFAULT_LAYOUT_MODE, config.layout))
        store.commit('TOGGLE_FIXED_HEADER', Vue.ls.get(DEFAULT_FIXED_HEADER, config.fixedHeader))
        store.commit('TOGGLE_FIXED_SIDERBAR', Vue.ls.get(DEFAULT_FIXED_SIDEMENU, config.fixSiderbar))
        store.commit('TOGGLE_CONTENT_WIDTH', Vue.ls.get(DEFAULT_CONTENT_WIDTH_TYPE, config.contentWidth))
        store.commit('TOGGLE_FIXED_HEADER_HIDDEN', Vue.ls.get(DEFAULT_FIXED_HEADER_HIDDEN, config.autoHideHeader))
        // store.commit('CLOSE_SIDEBAR', Vue.ls.get(CLOSE_SIDEBAR, false))
        // store.commit('TOGGLE_WEAK', Vue.ls.get(DEFAULT_COLOR_WEAK, config.colorWeak))
        store.commit('TOGGLE_COLOR', Vue.ls.get(DEFAULT_COLOR, config.primaryColor))
        store.commit('SET_TOKEN', Vue.ls.get(ACCESS_TOKEN))
        store.commit('SET_MULTI_PAGE', Vue.ls.get(DEFAULT_MULTI_PAGE, config.multipage))
    },
    render: h => h(App),
}).$mount('#app')
