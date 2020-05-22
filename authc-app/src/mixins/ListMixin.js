import Vue from 'vue'

import {ACCESS_TOKEN} from "../store/mutation-types";
import {deleteAction, getAction} from "../api/http";
import {filterObj} from "../utils/util";

export const ListMixin = {
    data(){
        return {
            tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
            /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
            queryParam: {},

            /**
             * 数据加载： lazy - 懒加载， immediate - 立即加载
             */
            loadingMode:'lazy',

            // 数据源
            dataSource: [],
            // 页面参数
            pagination:{
                current: 1,
                pageSize: 10,
                pageSizeOptions: ['10','20','30'],
                showTotal:(total, range) => {
                    return range[0]+"-"+range[1]+" 共"+ total + "条"
                },
                showQuickJumper: true,
                showSizeChanger: true,
                total: 0
            },
            //排序参数
            sorter:{
                column: 'createTime',
                order: 'desc'
            },
            // 筛选参数
            filters:{},
            // table加载状态
            loading: false,
            // table 选中的keys
            selectedRowKeys:[],
            // table 选中的records
            selectedRows:[],
            // 查询面板折叠
            toggleSearchStatus: false,
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(args){
            console.log("列表组件["+this.$options.name+"]数据加载方式 -- ", this.loadingMode)
            if(!this.url.list){
                this.$message.error("请设置url.list属性")
                return
            }

            if(this.loadingMode === 'lazy'){
                return;
            }

            // 加载数据 如果传入的参数是1， 则加载第一页的内容
            if(args === 1){
                this.pagination.current = 1
            }

            // 查询条件
            let params = this.getQueryParams()
            this.loading = true
            getAction(this.url.list, params).then((res) => {
                if(res.success){
                    this.dataSource = res.data.records
                    this.pagination.total = res.data.total
                }
                if(res.code === 510){
                    this.$message.warning(res.message)
                }
                this.loading = false
            })
        },
        initDictConfig(){
          console.log("--- ListMixin， 此方法由具体使用的组件实现 ---")
        },
        getQueryParams(){
            // 获取查询参数
            let sqp = {}
            if(this.superQueryParams){
                sqp['superQueryParams']=encodeURI(this.superQueryParams)
            }
            let param = Object.assign(sqp, this.queryParam, this.sorter ,this.filters);
            param.field = this.getQueryField();
            param.pageNo = this.pagination.current;
            param.pageSize = this.pagination.pageSize;
            return filterObj(param);
        },
        getQueryField() {
            //TODO 字段权限控制
        },
        doAdd(){
            this.$refs.modalForm.add()
            this.$refs.modalForm.title = "新增"
            this.$refs.modalForm.disableSubmit = false
        },
        doBatchDelete(){
            if(!this.url.deleteBatch){
                this.$message.error("请设置url.deleteBatch属性")
                return
            }
            if(this.selectedRowKeys.length <= 0){
                this.$message.warning('请选择一条记录！')
                return
            }else{
                let ids = ""
                for(let a=0;a<this.selectedRowKeys.length;a++){
                    ids += this.selectedRowKeys[a]+",";
                }
                let that = this
                this.$confirm({
                    title: '确认删除',
                    content: '是否删除选中数据？',
                    onOk:function(){
                        that.loading = true
                        deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                            if(res.success){
                                that.$message.success(res.message)
                                that.loadData()
                                that.onClearSelected()
                            }else{
                                that.$message.warning(res.message)
                            }
                        }).finally(()=>{
                            that.loading = false
                        })
                    }
                })
            }
        },
        doDelete(id){
            if(!this.url.delete){
                this.$message.error("请设置url.delete属性")
                return
            }
            let that = this
            deleteAction(that.url.delete, {id:id}).then((res) => {
                if(res.success){
                    that.$message.success(res.message)
                    that.loadData()
                }else{
                    this.$message.warning(res.message)
                }
            })
        },
        doEdit(record){
            this.$refs.modalForm.edit(record)
            this.$refs.modalForm.title = "编辑"
            this.$refs.modalForm.disableSubmit = false
        },
        showDetail(record){
            this.$refs.modalForm.edit(record)
            this.$refs.modalForm.title = "详情"
            this.$refs.modalForm.disableSubmit = true
        },
        modalSubmit(){
            this.loadData()
        },
        onClearSelected(){
            this.selectedRowKeys = []
            this.selectedRows = []
        },
        onSelectChange(selectedRowKeys, selectedRows){
            this.selectedRowKeys = selectedRowKeys
            this.selectedRows = selectedRows
        },
        searchQuery() {
            this.loadData(1);
        },
        searchReset() {
            this.queryParam = {}
            this.loadData(1);
        },
        tableRefresh(pagination, filters, sorter){
            // 分页， 排序， 筛选变化是触发
            if(Object.keys(sorter).length > 0){
                this.sorter.column = sorter.field
                this.sorter.order = "ascend" == sorter.order ? "asc": "desc"
            }
            this.pagination = pagination
            this.loadData()
        },
        handleToggleSearch(){
            this.toggleSearchStatus = !this.toggleSearchStatus
        }
    }

}
