import {deleteAction, getAction, postAction, putAction} from "../http";

const commonAPI = {
    fetchNotice:(parameter)=> getAction("/core/notice/fetch_header_notice", parameter),
    duplicateCheck : (parameter)=>getAction("/core/validate/duplicate/check", parameter),
    fetchDictItems: (parameter)=>getAction("/core/dict/item-list", parameter),
    fetchDictItemLabel:(parameter)=>getAction("/core/dict/item-label", parameter),
    addDict:(parameter)=>postAction("/core/dict/add",parameter),
    editDict:(parameter)=>putAction("/core/dict/edit",parameter),
    refreshDict:(parameter)=>putAction("/core/dict/refreshCache", parameter),
    deleteDict:(parameter)=>deleteAction("/core/dict/delete", parameter),
    addDictItem:(parameter)=>postAction("/core/dict/item/add", parameter),
    editDictItem:(parameter)=>putAction("/core/dict/item/edit", parameter),
    deleteDictItem:(parameter)=>postAction("/core/dict/item/delete", parameter),
}

export default commonAPI
