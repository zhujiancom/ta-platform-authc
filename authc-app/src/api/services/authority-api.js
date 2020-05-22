import {postAction, getAction} from "@/api/http";
import {putAction} from "../http";

const authority = {
    addRole:(params) =>  postAction('/sys/role/add',params),
    editRole:(params) => putAction('/sys/role/edit', params),
    queryPermissionsByUser: (params)=>getAction("/sys/permission/getUserPermissionByToken",params),
    queryAllRoles: (params)=>getAction("/sys/role/all-list", params),
    queryRolesByPage: (params)=>getAction("/sys/role/page-list", params),
    queryUserRole:(params)=>getAction("sys/role/user-role-list", params),
    addRoleUser:(params) => postAction('sys/role/adduser', params),
    getPermissionList:(params)=>getAction("/sys/permission/list",params),
    getPermissionTreeList:(params)=>getAction("/sys/permission/queryTreeList",params),
    addPermission:(params)=>postAction("sys/permission/add", params),
    editPermission:(params)=>putAction("sys/permission/edit", params),
}


export default authority
