<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="12" :sm="8">
                        <a-form-item label="角色名称" :labelCol="{span: 5}" :wrapperCol="{span:18, offset: 1}">
                            <a-input placeholder="请输入角色名称" v-model="queryParam.roleName"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="12" :sm="24">
                            <span style="float:left; overflow: hidden;" class="table-page-search-submitButtons">
                                <a-button type="primary" @click="searchQuery" icon="search"
                                          style="margin-left: 21px">查询</a-button>
                                <a-button type="primary" @click="searchReset" icon="reload"
                                          style="margin-left: 8px">重置</a-button>
                            </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator" style="border-top: 5px">
            <a-button @click="doAdd" type="primary" icon="plus">角色录入</a-button>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="doBatchDelete">删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作
                    <a-icon type="down"/>
                </a-button>
            </a-dropdown>
        </div>


        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"> </i> 已选择 <a><b>{{ selectedRowKeys.length
                }}</b></a>项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>

            <a-table
                    ref="table"
                    size="middle"
                    rowKey="id"
                    bordered
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="pagination"
                    :loading="loading"
                    :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type:'radio'}"
                    @change="tableRefresh"
            >
                    <span slot="action" slot-scope="text, record">
                        <a @click="openRoleUserList(record)">用户</a>
                        <a-divider type="vertical"/>

                        <a-dropdown>
                            <a class="ant-dropdown-link">
                                更多 <a-icon type="down"/>
                            </a>
                            <a-menu slot="overlay">
                                <a-menu-item>
                                  <a @click="handlePermission(record.id)">授权</a>
                                </a-menu-item>
                                <a-menu-item>
                                  <a @click="doEdit(record)">编辑</a>
                                </a-menu-item>
                                <a-menu-item>
                                  <a-popconfirm title="确定删除吗?" @confirm="() => doDelete(record.id)">
                                    <a>删除</a>
                                  </a-popconfirm>
                                </a-menu-item>
                            </a-menu>
                        </a-dropdown>
                    </span>
            </a-table>
        </div>

        <role-modal ref="modalForm" @ok="modalSubmit"></role-modal>
<!--        <role-user-map-list ref="userList"></role-user-map-list>-->
        <role-user-map-list-mixin ref="userList"></role-user-map-list-mixin>
    </a-card>
</template>
<script>
    import {ListMixin} from "../../mixins/ListMixin";
    import moment from "moment";
    import RoleModal from "./modal/RoleModal";
    import RoleUserMapListMixin from "./RoleUserMapListMixin";

    export default {
        name: 'RoleList',
        components: {
            RoleUserMapListMixin,
            RoleModal
        },
        mixins: [ListMixin],
        data() {
            return {
                loadingMode: 'immediate',
                columns: [
                    {
                        title: '角色编码',
                        align: 'center',
                        dataIndex: 'roleCode'
                    },
                    {
                        title: '角色名称',
                        align: 'center',
                        dataIndex: 'roleName'
                    },
                    {
                        title: '描述',
                        align: 'center',
                        dataIndex: 'description'
                    },
                    {
                        title: '创建时间',
                        dataIndex: 'createTime',
                        align: "center",
                        sorter: true,
                        customRender: (text) => {
                            return moment(text).format('YYYY-MM-DD hh:mm:ss')
                        }
                    },
                    {
                        title: '更新人',
                        dataIndex: 'updateBy',
                        align: "center",
                    },
                    {
                        title: '更新时间',
                        dataIndex: 'updateTime',
                        align: "center",
                        sorter: true,
                        customRender: (text) => {
                            if (text) {
                                return moment(text).format('YYYY-MM-DD hh:mm:ss')
                            }
                        }
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        scopedSlots: {customRender: 'action'}
                    }
                ],
                url: {
                    list: '/sys/role/page-list',
                    delete: 'sys/role/delete'
                }
            }
        },
        methods: {
            openRoleUserList(record) {
                this.$refs.userList.loadUsers(record)
            },
            handlePermission(id) {
                this.$refs.modalUserRole.show(id);
            },
        }
    }
</script>
<style scoped>
    /*@import '~@assets/less/common.less'*/
</style>
