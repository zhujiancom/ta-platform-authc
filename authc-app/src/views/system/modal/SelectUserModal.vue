<template>
    <a-modal :title="title"
             :width="800"
             :visible="visible"
             :confirm-loading="confirmLoading"
             @ok="handleOk"
             @cancel="handleCancel"
             cancelText="关闭"
             wrapClassName="ant-modal-cust-warp"
             style="top:5%;height: 85%;overflow-y: hidden">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="10">
                    <a-col :md="8" :sm="12">
                        <a-form-item label="用户账号">
                            <a-input placeholder="请输入用户账号" v-model="queryParam.username"></a-input>
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

        <!-- 表格区域 -->
        <a-table ref="table"
                 rowKey="id"
                 size="middle"
                 :columns="columns"
                 :dataSource="dataSource"
                 :pagination="pagination"
                 :loading="loading"
                 :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                 @change="tableRefresh"
        ></a-table>
    </a-modal>
</template>
<script>
    import {ListMixin} from "../../../mixins/ListMixin";
    import {filterObj} from "../../../utils/util";

    export default {
        name: 'SelectUserModal',
        mixins: [ListMixin],
        data() {
            return {
                visible: false,
                title: '选择已有用户',
                loading: false,
                confirmLoading: false,
                form: this.$form.createForm(this),
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
                        title: '性别',
                        align: "center",
                        width: 80,
                        dataIndex: 'sex_dictText',
                        sorter: true
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
                ],
                url: {
                    list: 'sys/user/page-list',
                }
            }
        },
        methods: {
            open() {
                this.loadingMode = 'immediate'
                this.loadData()
                this.visible = true
            },
            handleOk() {
                this.$emit("selectFinished", this.selectedRowKeys)
                this.visible = false
            },
            handleCancel() {
                this.visible = false
                this.$emit('close')
            },
            getQueryParams() {
                let param = {}
                param.status = 1
                param.pageNo = this.pagination.current
                param.pageSize = this.pagination.pageSize
                return filterObj(param);
            }
        }
    }
</script>
