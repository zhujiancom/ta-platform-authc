<template>
    <a-card>
        <!-- 按钮操作区域 -->
        <div class="table-operator">
            <a-button @click="doAdd" type="primary" icon="plus">新增</a-button>
            <a-button @click="doBatchDelete"
                      v-if="selectedRowKeys.length > 0"
                      ghost
                      type="primary"
                      icon="delete">批量删除
            </a-button>
        </div>

        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{
                selectedRowKeys.length }}</a>项&nbsp;&nbsp;
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>

            <a-table
                    :columns="columns"
                    size="middle"
                    :pagination="false"
                    :dataSource="dataSource"
                    :loading="loading"
                    :expandedRowKeys="expandedRowKeys"
                    @expandedRowsChange="handleExpandedRowsChange"
                    :row-selection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}">
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
                    <a href="javascript:;" @click="handleAddSub(record)">添加子菜单</a>
                  </a-menu-item>
                  <a-menu-item>
                    <a href="javascript:;" @click="handleDataRule(record)">数据规则</a>
                  </a-menu-item>

                  <a-menu-item>
                    <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                      <a>删除</a>
                    </a-popconfirm>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </span>
                <!-- 字符串超长截取省略号显示 -->
                <span slot="url" slot-scope="text">
              {{text | ellipsis }}
            </span>
                <!-- 字符串超长截取省略号显示-->
                <span slot="component" slot-scope="text">
              {{text | ellipsis }}
            </span>
            </a-table>
        </div>
        <permission-modal ref="modalForm" @ok="modalSubmit"></permission-modal>
    </a-card>
</template>
<script>
    import {ListMixin} from "../../mixins/ListMixin";
    import authority from "../../api/services/authority-api";
    import PermissionModal from "./modal/PermissionModal";

    const columns = [
        {
            title: '菜单名称',
            dataIndex: 'name',
            key: 'name'
        },
        {
            title: '菜单类型',
            dataIndex: 'menuType_dictText',
            key: 'menuType_dictText'
        },
        {
            title: '图标',
            dataIndex: 'icon',
            key: 'icon'
        },
        {
            title: '路由组件',
            dataIndex: 'component',
            key: 'component'
        },
        {
            title: '链接地址',
            dataIndex: 'url',
            key: 'url',
            scopedSlots: {customRender: 'url'}
        },
        {
            title: '排序',
            dataIndex: 'sortNo',
            key: 'sortNo'
        },
        {
            title: '操作',
            dataIndex: 'action',
            key: 'action',
            scopedSlots: {customRender: 'action'},
            align: 'center',
        },
    ]

    export default {
        name: 'PermissionList',
        components:{
            PermissionModal
        },
        data() {
            return {
                columns: columns,
                dataSource: [],
                loading: false,
                expandedRowKeys: [],
                loadingMode: 'immediate',
                url: {
                    list: '/sys/permission/list',
                    delete: '/sys/permission/delete',
                    deleteBatch: '/sys/permission/deleteBatch'
                }
            }
        },
        mixins: [ListMixin],
        methods: {
            loadData() {
                this.dataSource = []
                this.loading = true
                authority.getPermissionList().then((res) => {
                    if (res.success) {
                        // console.log(res.data)
                        this.dataSource = res.data
                    }
                }).finally(()=>{
                    this.loading =false
                })
            },
            handleExpandedRowsChange(expandedRows) {
                this.expandedRowKeys = expandedRows
            },
            handleAddSub(record) {
                console.log(record)
            },
            handleDataRule(record) {
                console.log(record)
            }
        }
    }
</script>
