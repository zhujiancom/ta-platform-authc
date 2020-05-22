<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">

                    <a-col :md="6" :sm="12">
                        <a-form-item label="账号">
                            <z-query-input placeholder="输入账号模糊查询" v-model="queryParam.username"></z-query-input>
                        </a-form-item>
                    </a-col>

                    <a-col :md="6" :sm="8">
                        <a-form-item label="性别">
                            <z-dict-view dict-code="sex" :show-tips="true" placeholder="请选择性别" v-model="queryParam.sex"></z-dict-view>
                        </a-form-item>
                    </a-col>

                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="真实名字">
                                <a-input placeholder="请输入真实名字" v-model="queryParam.realname"></a-input>
                            </a-form-item>
                        </a-col>

                        <a-col :md="6" :sm="8">
                            <a-form-item label="手机号码">
                                <a-input placeholder="请输入手机号码查询" v-model="queryParam.phone"></a-input>
                            </a-form-item>
                        </a-col>

                        <a-col :md="6" :sm="8">
                            <a-form-item label="用户状态">
                                <z-dict-view dict-code="user_status" :show-tips="true" placeholder="请选择用户状态" v-model="queryParam.status"></z-dict-view>
                            </a-form-item>
                        </a-col>
                    </template>

                    <a-col :md="6" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                          <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                          <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                          <a @click="handleToggleSearch" style="margin-left: 8px">
                            {{ toggleSearchStatus ? '收起' : '展开' }}
                            <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                          </a>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>

        <!-- 操作按钮区域 -->
        <div class="table-operator" style="border-top: 5px">
            <a-button @click="doAdd" type="primary" icon="plus">添加用户</a-button>
            <a-button type="primary" icon="download">导出</a-button>
            <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload>
            <a-button type="primary" icon="hdd" @click="recycleBinVisible=true">回收站</a-button>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay" @click="handleMenuClick">
                    <a-menu-item key="1">
                        <a-icon type="delete" @click="doBatchDelete"/>
                        删除
                    </a-menu-item>
                    <a-menu-item key="2">
                        <a-icon type="lock" @click="batchFrozen('2')"/>
                        冻结
                    </a-menu-item>
                    <a-menu-item key="3">
                        <a-icon type="unlock" @click="batchFrozen('1')"/>
                        解冻
                    </a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px">
                    批量操作
                    <a-icon type="down"/>
                </a-button>
            </a-dropdown>
        </div>

        <!-- table区域-begin -->
        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{
                selectedRowKeys.length }}</a>项&nbsp;&nbsp;
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>

            <a-table
                    ref="table"
                    bordered
                    size="middle"
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="pagination"
                    :loading="loading"
                    :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                    @change="tableRefresh">

                <template slot="avatarslot" slot-scope="text, record">
                    <div class="anty-img-wrap">
                        <a-avatar shape="square" :src="getAvatarView(record.avatar)" icon="user"/>
                    </div>
                </template>

                <span slot="action" slot-scope="text, record">
                  <a @click="doEdit(record)">编辑</a>

                  <a-divider type="vertical"/>

                  <a-dropdown>
                    <a class="ant-dropdown-link">
                      更多 <a-icon type="down"/>
                    </a>
                    <a-menu slot="overlay">
                      <a-menu-item>
                        <a href="javascript:;" @click="showDetail(record)">详情</a>
                      </a-menu-item>

                      <a-menu-item>
                        <a href="javascript:;" @click="handleChangePassword(record.username)">密码</a>
                      </a-menu-item>

                      <a-menu-item>
                        <a-popconfirm title="确定删除吗?" @confirm="() => doDelete(record.id)">
                          <a>删除</a>
                        </a-popconfirm>
                      </a-menu-item>

                      <a-menu-item v-if="record.status==1">
                        <a-popconfirm title="确定冻结吗?" @confirm="() => handleFrozen(record.id,2,record.username)">
                          <a>冻结</a>
                        </a-popconfirm>
                      </a-menu-item>

                      <a-menu-item v-if="record.status==2">
                        <a-popconfirm title="确定解冻吗?" @confirm="() => handleFrozen(record.id,1,record.username)">
                          <a>解冻</a>
                        </a-popconfirm>
                      </a-menu-item>

                      <a-menu-item>
                        <a href="javascript:;" @click="handleAgentSettings(record.username)">代理人</a>
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                </span>
            </a-table>
        </div>
        <!-- table区域-end -->

        <user-modal ref="modalForm" @ok="modalSubmit"></user-modal>
        <user-password ref="passwordmodal"></user-password>
        <user-recycle-bin-modal :visible.sync="recycleBinVisible" @ok="modalSubmit"/>
    </a-card>
</template>
<script>
    import {ListMixin} from "../../mixins/ListMixin";
    import ZQueryInput from "../../components/tools/ZQueryInput";
    import UserAPI from "../../api/services/user-api";
    import UserModal from "./modal/UserModal";
    import ZDictView from "../../components/tools/ZDictView";
    import {getFileAccessHttpUrl} from "../../utils/util";
    import UserPassword from "../../components/tools/UserPassword";
    import UserRecycleBinModal from "./modal/UserRecycleBinModal";

    export default {
        name: 'UserList',
        mixins: [ListMixin],
        components: {
            UserPassword,
            ZDictView,
            ZQueryInput,
            UserModal,
            UserRecycleBinModal,
        },
        data() {
            return {
                columns: [
                    {
                        title: '用户账号',
                        align: "center",
                        dataIndex: 'username',
                        width: 120
                    },
                    {
                        title: '用户姓名',
                        align: "center",
                        width: 100,
                        dataIndex: 'realname',
                    },
                    {
                        title: '头像',
                        align: "center",
                        width: 120,
                        dataIndex: 'avatar',
                        scopedSlots: {customRender: "avatarslot"}
                    },
                    {
                        title: '性别',
                        align: "center",
                        width: 80,
                        dataIndex: 'sex_dictText',
                        sorter: true
                    },
                    {
                        title: '生日',
                        align: "center",
                        width: 100,
                        dataIndex: 'birthday'
                    },
                    {
                        title: '手机号码',
                        align: "center",
                        width: 100,
                        dataIndex: 'phone'
                    },
                    {
                        title: '部门',
                        align: "center",
                        width: 180,
                        dataIndex: 'orgCode'
                    },
                    {
                        title: '状态',
                        align: "center",
                        width: 80,
                        dataIndex: 'status_dictText'
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        scopedSlots: {customRender: 'action'},
                        align: "center",
                        width: 170
                    }
                ],
                url: {
                    list: 'sys/user/page-list',
                    delete: 'sys/user/delete',
                    deleteBatch: 'sys/user/deleteBatch',
                    imgServer: window._CONFIG['staticDomainURL']
                },
                queryParam: {},
                loadingMode: 'immediate',
                recycleBinVisible: false
            }
        },
        methods: {
            handleMenuClick(e) {
                if (e.key == 1) {
                    this.doBatchDelete()
                } else if (e.key == 2) {
                    this.batchFrozen(2)
                } else if (e.key == 3) {
                    this.batchFrozen(1)
                }
            },
            batchFrozen(status) {
                if (this.selectedRowKeys.length <= 0) {
                    this.$message.warning('请选择一条记录！')
                    return false
                } else {
                    let ids = ""
                    let that = this
                    let isAdmin = false
                    for (let row of that.selectedRows) {
                        if (row.username == 'admin') {
                            isAdmin = true
                            break
                        }
                    }
                    if (isAdmin) {
                        that.$message.warning('管理员账号不允许此操作,请重新选择！')
                        return
                    }
                    that.selectedRowKeys.forEach(function (id) {
                        ids += id + ','
                    })
                    that.$confirm({
                        title: '确认操作',
                        content: '是否' + (status == 1 ? "解冻" : "冻结") + "选中账号？",
                        onOk: function () {
                            UserAPI.frozenBatch({ids: ids, status: status}).then((res) => {
                                if (res.success) {
                                    that.$message.success(res.message);
                                    that.loadData();
                                    that.onClearSelected();
                                } else {
                                    that.$message.warning(res.message);
                                }
                            })
                        }
                    })
                }
            },
            handleFrozen: function (id, status, username) {
                let that = this;
                if ('admin' == username) {
                    that.$message.warning('管理员账号不允许此操作！');
                    return;
                }
                UserAPI.frozenBatch({ids: id, status: status}).then((res) => {
                    if (res.success) {
                        that.$message.success(res.message);
                        that.loadData();
                    } else {
                        that.$message.warning(res.message);
                    }
                });
            },
            handleChangePassword(username) {
                this.$refs.passwordmodal.show(username);
            },
            handleAgentSettings(username) {
                console.log(username)
            },
            getAvatarView(avatar){
                return getFileAccessHttpUrl(avatar,this.url.imgServer,"http")
            }
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
