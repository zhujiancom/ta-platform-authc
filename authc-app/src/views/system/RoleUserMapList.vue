<template>
    <a-card :bordered="false">
        <a-drawer title='已选用户列表'
                  :visible="visible"
                  :width="screenWidth"
                  @close="handleClose"
        >
            <div :style="{ padding: '10px', border:'1px solid #e9e9e9', background: '#fff'}">
                <div class="table-page-search-wrapper">
                    <a-form layout="inline" @keyup.enter.native="searchQuery">
                        <a-row :gutter="10">
                            <a-col :md="8" :sm="12">
                                <a-form-item label="用户账号">
                                    <a-input placeholder="请输入账号" v-model="queryParam.username"></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col :md="8" :sm="12">
                                <a-form-item label="用户名称">
                                    <a-input placeholder="请输入名称" v-model="queryParam.realname"></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col :md="7" :sm="24">
                              <span style="float: left;" class="table-page-search-submitButtons">
                                <a-button type="primary" @click="searchQuery">搜索</a-button>
                                <a-button type="primary" @click="searchReset" style="margin-left: 8px">重置</a-button>
                                </span>
                            </a-col>
                        </a-row>
                    </a-form>
                </div>

                <div class="table-operator" :md="24" :sm="24" style="margin-bottom: 20px">
                    <a-button @click="handleAddUserRole" type="primary" icon="plus" style="margin-top: 16px">添加已有用户</a-button>
                    <a-dropdown v-if="selectedRowKeys.length > 0">
                        <a-menu slot="overlay">
                            <a-menu-item key="1" @click="doBatchDelete">删除</a-menu-item>
                        </a-menu>
                        <a-button style="margin-left: 8px"> 批量操作
                            <a-icon type="down"/>
                        </a-button>
                    </a-dropdown>
                </div>

                <!-- 表格区域 -->
                <div>
                    <a-table
                            ref="table"
                            rowKey="id"
                            size="middle"
                            :columns="columns"
                            :dataSource="dataSource"
                            :pagination="pagination"
                            :loading="loading"
                            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                            @change="tableRefresh"
                    >
                        <span slot="action" slot-scope="text, record">
                            <a @click="doEdit(record)">编辑</a>
                            <a-divider type="vertical"/>
                            <a-popconfirm title="确定删除吗?" @confirm="() => doDelete(record.id)">
                              <a>删除</a>
                            </a-popconfirm>
                          </span>
                    </a-table>
                </div>
            </div>
        </a-drawer>
        <user-modal ref="modalForm" @ok="submitForm"></user-modal>
        <select-user-modal ref="selectUserModal" @selectFinished="selectOK"></select-user-modal>
    </a-card>
</template>
<script>
    import {deleteAction, getAction} from "../../api/http";
    import {filterObj} from "../../utils/util";
    import UserModal from "./modal/UserModal";
    import SelectUserModal from "./modal/SelectUserModal";
    import authorityAPI from "../../api/services/authority-api";

    export default {
        name: 'RoleUserMapList',
        components: {SelectUserModal, UserModal},
        data() {
            return {
                visible: false,
                screenWidth: 800,
                loading: false,
                form: this.$form.createForm(this),
                queryParam:{},
                roleId: "",
                url: {
                    list: "/sys/role/page-list-by-role",
                    delete: "sys/role/userrole/delete"
                },
                dataSource:[],
                selectedRowKeys:[],
                selectedRows:[],
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
                labelCol: {
                    xs: {span: 5},
                    sm: {span: 5},
                },
                wrapperCol: {
                    xs: {span: 12},
                    sm: {span: 12},
                },
                columns: [
                    {
                        title: '用户账号',
                        align: "center",
                        dataIndex: 'userName',
                    },
                    {
                        title: '用户名称',
                        align: "center",
                        dataIndex: 'realName',
                    },
                    {
                        title: '工号',
                        align: "center",
                        dataIndex: 'workNo',
                    },
                    {
                        title: '状态',
                        align: "center",
                        dataIndex: 'status_dictText',
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: "center",
                        scopedSlots: {customRender: 'action'},
                    }
                ],
            }
        },
        created() {
            // 当页面初始化时,根据屏幕大小来给抽屉设置宽度
            this.resetScreenSize()
        },
        methods:{
            doEdit(record){
                this.$refs.modalForm.edit({id:record.userId})
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
            handleClose(){
                this.visible = false
                this.$emit("close")
                this.dataSource = []
                this.selectedRowKeys = []
                this.selectedRows = []
            },
            searchQuery(){
                this.loadData(1);
            },
            searchReset(){
                this.queryParam = {}
                this.loadData(1)
            },
            loadUsers(record){
                this.visible = true
                if(record.id){
                    this.roleId = record.id
                    this.loadData()
                }else{
                    this.$message.warning("角色数据异常！")
                }
            },
            loadData(args){
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
                }).finally(()=>{
                    this.loading = false
                })
            },
            // 抽屉的宽度随着屏幕大小来改变
            resetScreenSize() {
                let screenWidth = document.body.clientWidth;
                if (screenWidth < 600) {
                    this.screenWidth = screenWidth
                } else {
                    this.screenWidth = 600
                }
            },
            getQueryParams(){
                // 获取查询参数
                let param = Object.assign({}, this.queryParam)
                param.roleId = this.roleId
                param.pageNo = this.pagination.current
                param.pageSize = this.pagination.pageSize
                return filterObj(param);
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
            submitForm(){
                this.loadData()
            },
            onSelectChange(selectedRowKeys, selectedRows){
                this.selectedRowKeys = selectedRowKeys
                this.selectedRows = selectedRows
            },
            handleAddUserRole(){
                this.$refs.selectUserModal.open()
            },
            selectOK(data) {
                let params = {}
                params.roleId = this.roleId
                params.userIds = []
                for (let a = 0; a < data.length; a++) {
                    params.userIds.push(data[a])
                }
                console.log("params = ",params)
                authorityAPI.addRoleUser(params).then((res) => {
                    if (res.success) {
                        this.loadData()
                        this.$message.success(res.message)
                    } else {
                        this.$message.warning(res.message)
                    }
                })
            },
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
