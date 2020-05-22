<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">

                    <a-col :span="6">
                        <a-form-item label="字典名称">
                            <a-input placeholder="请输入字典名称" v-model="queryParam.dictName"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-item label="字典编码">
                            <a-input placeholder="请输入字典编码" v-model="queryParam.dictCode"></a-input>
                        </a-form-item>
                    </a-col>

                    <a-col :span="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                            <a-button type="primary" @click="searchReset" icon="reload"
                                      style="margin-left: 8px">重置</a-button>
                        </span>
                    </a-col>

                </a-row>
            </a-form>
        </div>

        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button @click="doAdd" type="primary" icon="plus">新增</a-button>
            <a-button @click="refreshCache" type="primary" icon="sync">刷新缓存</a-button>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="doBatchDelete">
                        <a-icon type="delete"/>
                        删除
                    </a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作
                    <a-icon type="down"/>
                </a-button>
            </a-dropdown>
        </div>

        <!-- table区域-begin -->
        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
                selectedRowKeys.length }}</a>项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>

            <a-table
                    ref="table"
                    size="middle"
                    bordered
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="pagination"
                    :loading="loading"
                    :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                    @change="tableRefresh">

        <span slot="action" slot-scope="text, record">
            <a @click="doEdit(record)"><a-icon type="edit"/>编辑</a>
            <a-divider type="vertical"/>
            <a @click="editDictItem(record)"><a-icon type="setting"/>字典配置</a>
            <a-divider type="vertical"/>
              <a-popconfirm title="确定删除吗?" @confirm="() =>doDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
        </span>

            </a-table>
        </div>
        <!-- table区域-end -->

        <sys-dict-modal ref="modalForm" @ok="modalSubmit"></sys-dict-modal>
        <sys-dict-item-list ref="dictItemList"></sys-dict-item-list>
    </a-card>
</template>
<script>
    import {ListMixin} from "../../mixins/ListMixin";
    import SysDictModal from "./modal/SysDictModal";
    import api from "../../api";
    import SysDictItemList from "./SysDictItemList";

    export default {
        name: 'SysDictList',
        components: {SysDictItemList, SysDictModal},
        mixins: [ListMixin],
        data() {
            return {
                columns:[
                    {
                        title: '#',
                        dataIndex: '',
                        key: 'rowIndex',
                        width: 120,
                        align: "center",
                        customRender: function (t, r, index) {
                            return parseInt(index) + 1;
                        }
                    },
                    {
                        title: '字典名称',
                        align: "left",
                        dataIndex: 'dictName',
                    },
                    {
                        title: '字典编号',
                        align: "left",
                        dataIndex: 'dictCode',
                    },
                    {
                        title: '字典类型',
                        align: "left",
                        dataIndex: 'type',
                        customRender:function(val){
                            let text = "文本型"
                            if(val == "1"){
                                text = "数值型"
                            }
                            return text
                        }
                    },
                    {
                        title: '描述',
                        align: "left",
                        dataIndex: 'description',
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: "center",
                        scopedSlots: {customRender: 'action'},
                    }
                ],
                url:{
                    list: '/core/dict/page-list',
                    delete: '/core/dict/delete'
                },
                queryParam: {},
                loadingMode: 'immediate'
            }
        },
        methods: {
            editDictItem(record){
                console.log(record)
                this.$refs.dictItemList.loadItems(record)
                this.$refs.dictItemList.title=record.dictName
            },
            refreshCache(){
                api.commonAPI.refreshDict().then((res)=>{
                    if(res.success){
                        this.$message.success(res.message)
                    }
                }).catch(e=>{
                    this.$message.warning("刷新缓存失败")
                    console.log(e)
                }).finally(()=>{
                    this.loadData(1)
                })
            },
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
