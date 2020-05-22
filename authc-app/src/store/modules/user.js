import Vue from 'vue'

import {ACCESS_TOKEN, USER_NAME, USER_INFO} from "@/store/mutation-types";
import { welcome } from "@/utils/util"
import userAPI from "../../api/services/user-api";
import authorityAPI from "../../api/services/authority-api";
import {SYS_BUTTON_AUTH, USER_AUTH} from "../mutation-types";

const user ={
    state: {
        token: '',
        username: '',
        realname: '',
        welcome: '',
        avatar: '',
        permissionList: [],
        info: {}
    },
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token
        },
        SET_NAME: (state, { username, realname, welcome }) => {
            state.username = username
            state.realname = realname
            state.welcome = welcome
        },
        SET_AVATAR: (state, avatar) => {
            state.avatar = avatar
        },
        SET_PERMISSIONLIST: (state, permissionList) => {
            state.permissionList = permissionList
        },
        SET_INFO: (state, info) => {
            state.info = info
        },
    },
    actions: {
        Login({commit}, userInfo){
            return new Promise((resolve, reject) => {
                userAPI.login(userInfo).then(response => {
                    if(response.code == '200'){
                        const result = response.data;
                        const userInfo = result.userInfo;
                        Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
                        Vue.ls.set(USER_NAME, userInfo.username, 7 * 24 * 60 * 60 * 1000)
                        Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
                        commit('SET_TOKEN', result.token)
                        commit('SET_INFO', userInfo)
                        commit('SET_NAME', { username: userInfo.username,realname: userInfo.realname, welcome: welcome() })
                        commit('SET_AVATAR', userInfo.avatar)
                        resolve(response)
                    }else{
                        reject(response)
                    }
                }).catch(error => {
                    reject(error)
                })
            })
        },
        Logout({commit, state}){
            return new Promise((resolve => {
                let logoutToken = state.token;
                commit('SET_TOKEN','')
                commit('SET_PERMISSIONLIST', [])
                Vue.ls.remove(ACCESS_TOKEN)
                userAPI.logout(logoutToken).then(() => {
                    resolve()
                }).catch(()=>{
                    resolve()
                })
            }))
        },
        // 获取用户信息
        GetPermissionList({ commit }) {
            return new Promise((resolve, reject) => {
                let v_token = Vue.ls.get(ACCESS_TOKEN);
                let params = {token:v_token};
                authorityAPI.queryPermissionsByUser(params).then(response => {
                    const menuData = response.data.menu;
                    const authData = response.data.auth;
                    const allAuthData = response.data.allAuth;
                    //Vue.ls.set(USER_AUTH,authData);
                    sessionStorage.setItem(USER_AUTH,JSON.stringify(authData));
                    sessionStorage.setItem(SYS_BUTTON_AUTH,JSON.stringify(allAuthData));
                    if (menuData && menuData.length > 0) {
                        //update--begin--autor:qinfeng-----date:20200109------for：JEECG-63 一级菜单的子菜单全部是隐藏路由，则一级菜单不显示------
                        menuData.forEach((item) => {
                            if (item["children"]) {
                                let hasChildrenMenu = item["children"].filter((i) => {
                                    return !i.hidden || i.hidden == false
                                })
                                if (hasChildrenMenu == null || hasChildrenMenu.length == 0) {
                                    item["hidden"] = true
                                }
                            }
                        })
                        console.log(" menu show json ", menuData)
                        //update--end--autor:qinfeng-----date:20200109------for：JEECG-63 一级菜单的子菜单全部是隐藏路由，则一级菜单不显示------
                        commit('SET_PERMISSIONLIST', menuData)
                    } else {
                        reject('getPermissionList: permissions must be a non-null array !')
                    }
                    resolve(response)
                }).catch(error => {
                    reject(error)
                })
            })
        }
    }
}

export default user
