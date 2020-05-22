<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">

                    <a-col :span="6">
                        <a-form-item label="标题">
                            <a-input placeholder="请输入标题" v-model="queryParam.titile"></a-input>
                        </a-form-item>
                    </a-col>

<!--                    <a-col :span="6">-->
<!--                      <a-form-item label="内容">-->
<!--&lt;!&ndash;                        <a-input placeholder="请输入内容" v-model="queryParam.msgContent"></a-input>&ndash;&gt;-->
<!--                      </a-form-item>-->
<!--                    </a-col>-->

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
          <a v-if="record.publishState == 0" @click="doEdit(record)">编辑</a>

          <a-divider type="vertical" v-if="record.publishState == 0"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item v-if="record.publishState != 1">
                <a-popconfirm title="确定删除吗?" @confirm="() => doDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-if="record.publishState == 0">
                <a-popconfirm title="确定发布吗?" @confirm="() => doPublish(record.id)">
                  <a>发布</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-if="record.publishState == 1">
                <a-popconfirm title="确定撤销吗?" @confirm="() => doRevoke(record.id)">
                  <a>撤销</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item>
                  <a @click="showDetail(record)">查看</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

            </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <sys-announcement-modal ref="modalForm" @ok="modalSubmit"></sys-announcement-modal>
    </a-card>
</template>
<script>
    import {ListMixin} from "../../mixins/ListMixin";
    import SysAnnouncementModal from "./modal/SysAnnouncementModal";

    const columns = [
        {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function (t, r, index) {
                return parseInt(index) + 1
            }
        },
        {
            title: '标题',
            dataIndex: 'title',
            align: 'center',
        },
        {
            title: '消息类型',
            dataIndex: 'category_dictText',
            align: 'center',
        },
        {
            title: '发布人',
            align: "center",
            dataIndex: 'sender'
        },
        {
            title: '优先级',
            align: "center",
            dataIndex: 'priority_dictText',
        },
        {
            title: '通告对象',
            align: "center",
            dataIndex: 'receiverType_dictText',
        },
        {
            title: '发布状态',
            align: "center",
            dataIndex: 'publishState_dictText',
        },
        {
            title: '发布时间',
            align: "center",
            dataIndex: 'sendTime'
        },
        {
            title: '撤销时间',
            align: "center",
            dataIndex: 'cancelTime'
        },
        {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {customRender: 'action'},
        }
    ]

    export default {
        name: 'SysAnnouncementList',
        mixins: [ListMixin],
        components: {
            SysAnnouncementModal
        },
        data() {
            return {
                description: '系统通告管理页面',
                queryParam: {},
                columns,
                url: {
                    list: "/core/notice/list",
                    delete: "/core/notice/delete",
                    deleteBatch: "/sys/notice/deleteBatch",
                    releaseDataUrl: "/sys/notice/doReleaseData",
                    revokeDataUrl: "sys/notice/doRevokeData",
                    exportXlsUrl: "sys/notice/exportXls",
                    importExcelUrl: "sys/notice/importExcel",
                }
            }
        },
        computed: {
            importExcelUrl: function () {
                return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
            }
        },
        methods: {
            // 发布通告
            doPublish: function (id) {
                console.log("--- publish announce id : " + id)
            },
            doRevoke: function (id) {
                console.log("--- revoke announce id : ", id)
            },
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
