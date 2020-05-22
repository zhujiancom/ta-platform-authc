import {postAction, getAction, putAction} from "../http";
// import base from "../base";
import {axios} from "@/utils/request";

const userAPI = {
    login: (parameter) => postAction('/sys/login', parameter),

    phoneLogin: (parameter) => postAction('/sys/phoneLogin', parameter),

    logout:(token)=>{
        return axios({
            url:'/sys/logout',
            method: 'post',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'X-Access-Token': token
            }
        })
    },
    //验证用户是否存在
    checkOnlyUser: (params)=>getAction("/sys/user/checkOnlyUser",params),
    frozenBatch : (params)=>putAction("/sys/user/frozenBatch",params),
    addUser: (parameter)=>postAction("/sys/user/add", parameter),
    editUser: (parameter)=>putAction("/sys/user/edit", parameter),
    queryUserInfo:(parameter)=>getAction("/sys/user/info", parameter),
    queryDepartTreeList:(parameter)=>getAction("/sys/depart/queryTreeList", parameter),
    addDepart:(parameter)=>postAction("/sys/depart/add", parameter),
    editDepart:(parameter)=>putAction("/sys/depart/edit", parameter),
}

export default userAPI
