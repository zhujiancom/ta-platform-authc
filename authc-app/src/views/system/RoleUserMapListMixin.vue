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
        <user-modal ref="modalForm" @ok="modalSubmit"></user-modal>
        <select-user-modal ref="selectUserModal" @selectFinished="selectOK"></select-user-modal>
    </a-card>
</template>
<script>
    import UserModal from "./modal/UserModal";
    import SelectUserModal from "./modal/SelectUserModal";
    import authorityAPI from "../../api/services/authority-api";
    import {ListMixin} from "../../mixins/ListMixin";

    export default {
        name: 'RoleUserMapListMixin',
        components: {SelectUserModal, UserModal},
        mixins:[ListMixin],
        data() {
            return {
                visible: false,
                screenWidth: 800,
                loading: false,
                form: this.$form.createForm(this),
                roleId: "",
                url: {
                    list: "/sys/role/page-list-by-role",
                    delete: "sys/role/userrole/delete"
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
            loadUsers(record){
                this.visible = true
                this.loadingMode = 'immediate'
                if(record.id){
                    this.roleId = record.id
                    this.filters = {roleId:record.id}
                    this.loadData()
                }else{
                    this.$message.warning("角色数据异常！")
                }
            },
            doEdit(record){
                this.$refs.modalForm.edit({id:record.userId})
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
            handleClose(){
                this.visible = false
                this.$emit("close")
            },
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
