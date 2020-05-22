<template>
    <a-card :bordered="false">
        <a-drawer :title='title'
                  :visible="visible"
                  :width="screenWidth"
                  @close="handleClose"
        >
            <div :style="{ padding: '10px', border:'1px solid #e9e9e9', background: '#fff'}">
                <div class="table-page-search-wrapper">
                    <a-form layout="inline" @keyup.enter.native="searchQuery">
                        <a-row :gutter="10">
                            <a-col :md="8" :sm="12">
                                <a-form-item label="名称">
                                    <a-input style="width: 120px;" placeholder="请输入名称" v-model="queryParam.itemText"></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col :md="9" :sm="24">
                                <a-form-item label="状态" style="width: 170px" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                    <z-dict-view dict-code="valid_status" :show-tips="true" placeholder="启用状态" v-model="queryParam.status"></z-dict-view>
                                </a-form-item>
                            </a-col>
                            <a-col :md="7" :sm="24">
                              <span style="float: left;" class="table-page-search-submitButtons">
                                <a-button type="primary" @click="searchQuery">搜索</a-button>
                                <a-button type="primary" @click="searchReset" style="margin-left: 8px">重置</a-button>
                                </span>
                            </a-col>
                        </a-row>
                        <a-row>
                            <a-col :md="2" :sm="24">
                                <a-button style="margin-bottom: 10px" type="primary" @click="doAdd">新增</a-button>
                            </a-col>
                        </a-row>
                    </a-form>
                </div>

                <div>
                    <a-table
                            ref="table"
                            rowKey="id"
                            size="middle"
                            :columns="columns"
                            :dataSource="dataSource"
                            :pagination="pagination"
                            :loading="loading"
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
        <sys-dict-item-modal ref="modalForm" :dictId="dictId" @ok="modalSubmit"></sys-dict-item-modal>
    </a-card>
</template>
<script>
    import ZDictView from "../../components/tools/ZDictView";
    import SysDictItemModal from "./modal/SysDictItemModal";
    import {ListMixin} from "../../mixins/ListMixin";

    export default {
        name:'SysDictItemList',
        mixins:[ListMixin],
        components: {SysDictItemModal, ZDictView},
        data() {
            return {
                title: "操作",
                visible: false,
                screenWidth: 800,
                queryParam:{},
                dataSource: [],
                loading: false,
                dictId:"",
                labelCol: {
                    xs: {span: 5},
                    sm: {span: 5},
                },
                wrapperCol: {
                    xs: {span: 12},
                    sm: {span: 12},
                },
                form: this.$form.createForm(this),
                validatorRules: {
                    itemText: {rules: [{required: true, message: '请输入名称!'}]},
                    itemValue: {rules: [{required: true, message: '请输入数据值!'}]},
                },
                url: {
                    list: "/core/dict/item/page-list",
                    delete: "/core/dict/item/delete",
                },
                columns: [
                    {
                        title: '名称',
                        align: "center",
                        dataIndex: 'itemText',
                    },
                    {
                        title: '数据值',
                        align: "center",
                        dataIndex: 'itemValue',
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
            loadItems(record){
                this.visible = true
                this.loadingMode = true
                if(record.id){
                    this.dictId = record.id
                    this.filters = {dictId:record.id}
                    this.loadData()
                }else{
                    this.$message.warning("字典项数据异常！")
                }
            },
            handleClose(){
                this.visible = false
                this.form.resetFields()
                this.dataSource = []
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
        }
    }
</script>
