import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import user from './modules/user'
import permission from "./modules/permission";
import {USER_INFO} from "./mutation-types";

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        app,
        user,
        permission
    },
    state: {
        network: true
    },
    mutations: {
        changeNetwork: (state, value) =>{
            state.network = value
        }
    },
    actions: {

    },
    getters: {
        permissionList: state => state.user.permissionList,
        addRouters: state => state.permission.addRouters,
        token: state => state.user.token,
        nickname: state => {state.user.realname = Vue.ls.get(USER_INFO).realname; return state.user.realname},
        avatar: state => {state.user.avatar = Vue.ls.get(USER_INFO).avatar; return state.user.avatar},
        userInfo: state => {state.user.info = Vue.ls.get(USER_INFO); return state.user.info},
    }
})
