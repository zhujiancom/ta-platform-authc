import {isNull} from "../utils/validate";
import {SYS_BUTTON_AUTH, USER_AUTH} from "../store/mutation-types";

const hasPermission = {
    install(Vue, options){
        console.log("hasPermission options = ",options);
        Vue.directive('has',{
            inserted:(el, binding, vnode) => {
                console.time()
                // 权限处理， 如果命中， 则不进行全局权限处理
                if(!filterNodePermission(el, binding, vnode)){
                    filterGlobalPermission(el, binding, vnode);
                }
                console.timeEnd()
            }
        })
    }
}

/**
 * 流程节点权限控制
 * @param el
 * @param binding
 * @param vnode
 */
export function filterNodePermission(el, binding, vnode){
    console.log("流程节点页面权限--NODE--");
    let permissionList = []
    try{
        let obj = vnode.context.formData
        if(obj){
            let bpmList = obj.permissionList
            for(let bpm of bpmList){
                if(bpm.type != '2'){
                    permissionList.push(bpm)
                }
            }
        }else{
            return false
        }
    }catch (e) {
        console.log("流程表单页面权限异常----", e)
    }
    if(isNull(permissionList)){
        return false
    }

    let permissions = []
    for(let item of permissionList){
        if(item.type != '2'){
            permissions.push(item.action)
        }
    }

    console.log("页面权限permission ----"+permissions)
    console.log("页面权限binding.value ----"+binding.value)

    if(!permissions.includes(binding.value)){
        return false
    }else{
        for(let item of permissionList){
            if(binding.value === item.action){
                return true
            }
        }
    }
    return false
}

export function filterGlobalPermission(el, binding, vnode){
    console.log("全局页面权限--Global--");

    let permissionList = []
    let allPermissionList = []

    let authList = JSON.parse(sessionStorage.getItem(USER_AUTH) || "[]")
    for(let auth of authList){
        if(auth.type != '2'){
            permissionList.push(auth)
        }
    }

    let allAuthList = JSON.parse(sessionStorage.getItem(SYS_BUTTON_AUTH || "[]"))
    for(let auth of allAuthList){
        if(auth.type != '2'){
            allPermissionList.push(auth)
        }
    }

    // 设置全局配置是否有命中
    let invalidFlag = false // 无效命中
    if(!isNull(allPermissionList)){
        for(let item of allPermissionList){
            if(binding.value === item.action){
                if(item.status == '0'){
                    invalidFlag = true
                    break
                }
            }
        }
        if(invalidFlag){
            return
        }
    }else{
        el.parentNode.removeChild(el)
        return
    }

    let permissions = []
    for(let item of permissionList){
        if(item.type != '2'){
            permissions.push(item.action)
        }
    }
    if(!permissions.includes(binding.value)){
        el.parentNode.removeChild(el)
    }


}

export default hasPermission
