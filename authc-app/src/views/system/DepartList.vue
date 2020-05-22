<template>
    <a-row :gutter="10">
        <a-col :md="12" :sm="24">
            <!-- 部门列表 -->
            <a-card :bordered="false">
                <!-- 按钮操作区域 -->
                <a-row style="margin-left: 14px">
                    <a-button @click="handleAdd(1)" type="primary">添加一级部门</a-button>
                    <a-button @click="handleAdd(2)" type="primary">添加子部门</a-button>
                    <a-button title="删除多条数据" @click="batchDelete" type="default">
                        <a-icon type="plus"/>
                        批量删除
                    </a-button>
                </a-row>
                <div style="background: #fff;padding-left:16px;height: 100%; margin-top: 5px">
                    <a-alert type="info" :showIcon="true">
                        <div slot="message">
                            当前选择:
                            <template v-if="this.currSelected.title">
                                <span><strong>{{this.currSelected.title}}</strong></span>
                                <a style="margin-left: 10px" @click="onClearSelected">取消选择</a>
                            </template>
                        </div>
                    </a-alert>
                    <a-input-search @search="onSearch" style="width:100%;margin-top: 10px" placeholder="请输入部门名称"/>
                    <a-row>
                        <a-col :md="5" :sm="12">
                            <div class="drawer-tree-operator-button">
                                <a-dropdown :trigger="['click']" placement="topCenter">
                                    <a-menu slot="overlay">
                                        <a-menu-item key="1" @click="switchCheckStrictly(1)">级联选择</a-menu-item>
                                        <a-menu-item key="2" @click="switchCheckStrictly(2)">取消级联</a-menu-item>
                                        <a-menu-item key="3" @click="checkAll">全部勾选</a-menu-item>
                                        <a-menu-item key="4" @click="cancelCheckAll">取消全选</a-menu-item>
                                        <a-menu-item key="5" @click="expandAll">展开所有</a-menu-item>
                                        <a-menu-item key="6" @click="closeAll">合并所有</a-menu-item>
                                    </a-menu>
                                    <a-button>
                                        操作
                                        <a-icon type="up"/>
                                    </a-button>
                                </a-dropdown>
                            </div>
                        </a-col>
                    </a-row>
                    <!-- 树 -->
                    <a-col :md="12" :sm="24">
                        <template :trigger="this.dropTrigger" @visibleChange="dropStatus">
                            <span style="user-select: none">
                                <a-tree
                                        checkable
                                        multiple
                                        @select="onSelect"
                                        @check="onCheck"
                                        @rightClick="rightHandle"
                                        :selectedKeys="selectedKeys"
                                        :checkedKeys="checkedKeys"
                                        :treeData="departTree"
                                        :checkStrictly="checkStrictly"
                                        :expandedKeys="expandedKeys"
                                        :autoExpandParent="autoExpandParent"
                                        @expand="onExpand"/>
                            </span>
                            <!--新增右键点击事件,和增加添加和删除功能-->
                            <a-menu slot="overlay">
                                <a-menu-item @click="handleAdd(3)" key="1">添加</a-menu-item>
                                <a-menu-item @click="handleDelete" key="2">删除</a-menu-item>
                                <a-menu-item @click="closeDrop" key="3">取消</a-menu-item>
                            </a-menu>
                        </template>
                    </a-col>
                </div>
            </a-card>
        </a-col>
        <a-col :md="12" :sm="24">
            <!-- 部门详情 -->
            <a-tabs defaultActiveKey="1">
                <a-tab-pane tab="基本信息" key="1">
                    <depart-base-info :curr-selected="currSelected" :depart-tree="departTree" @reloadTree="loadTree"></depart-base-info>
                </a-tab-pane>
                <a-tab-pane tab="部门权限" key="2" forceRender>
                    <depart-auth-info ref="departAuth"></depart-auth-info>
                </a-tab-pane>
            </a-tabs>
        </a-col>

        <depart-modal ref="departModal" @ok="loadTree"></depart-modal>
    </a-row>
</template>
<script>

    import DepartModal from "./modal/DepartModal";
    import userAPI from "../../api/services/user-api";
    import DepartBaseInfo from "./panes/DepartBaseInfo";
    import DepartAuthInfo from "./panes/DepartAuthInfo";

    export default {
        name: 'DepartList',
        components: {DepartAuthInfo, DepartBaseInfo, DepartModal},
        data() {
            return {
                currSelected: {},
                departTree: [],
                expandedKeys: [],
                autoExpandParent: true,
                checkStrictly: true,
                selectedKeys: [],
                checkedKeys: [],
                allTreeKeys: [],
                rightClickSelectedKey: {},
                dropTrigger: false,
            }
        },
        created() {
            this.loadTree()
        },
        methods: {
            loadTree() {
                this.departTree = []
                this.allTreeKeys = []
                let that = this
                userAPI.queryDepartTreeList().then((res) => {
                    if (res.success) {
                        let result = JSON.parse(res.data)
                        // console.log("result ", result)
                        for (let i = 0; i < result.length; i++) {
                            let temp = result[i]
                            that.departTree.push(temp)
                            that.getAllKeys(temp)
                        }
                    }
                }).finally(() => {
                    this.loading = false
                })
            },
            onSearch() {

            },
            onClearSelected() {
                this.hiding = true
                this.checkedKeys = []
                this.currSelected = {}
                this.form.resetFields()
                this.selectedKeys = []
                this.$refs.departAuth.departId = ''
            },
            batchDelete() {

            },
            handleAdd(key) {
                console.log("key = ", key)
                if (key == 1) {
                    this.$refs.departModal.title = "新增一级部门"
                    this.$refs.departModal.add()
                }
                if (key == 2) {
                    console.log("this.currSelected = ", this.currSelected)
                    let key = this.currSelected.key
                    if (!key) {
                        this.$message.warning("请先选择一个机构")
                        return false
                    }
                    this.$refs.departModal.title = "新增子部门"
                    this.$refs.departModal.add(this.currSelected)
                } else if(key==3){
                    console.log("else ")
                    this.$refs.departModal.add(this.rightClickSelectedKey)
                    this.$refs.departModal.title = '新增子部门'
                }
            },
            handleDelete() {

            },
            closeDrop() {

            },
            getAllKeys(node) {
                this.allTreeKeys.push(node.key)
                if (node.children && node.children.length > 0) {
                    for (let a = 0; a < node.children.length; a++) {
                        this.getAllKeys(node.children[a])
                    }
                }
            },
            expandAll() {
                this.expandedKeys = this.allTreeKeys
            },
            closeAll() {
                this.expandedKeys = []
            },
            onSelect(selectedKeys, info) {
                // console.log("selected", selectedKeys, info)
                let record = info.node.dataRef
                // console.log('record', record)
                this.currSelected = Object.assign({}, record)
                this.model = this.currSelected
                this.selectedKeys = [record.key]
                this.$refs.departAuth.show(record.id);
            },
            onCheck(checkedKeys, info) {
                console.log('onCheck', checkedKeys, info)
                if (this.checkStrictly) {
                    this.checkedKeys = checkedKeys.checked;
                } else {
                    this.checkedKeys = checkedKeys
                }
            },
            rightHandle(node) {
                this.dropTrigger = 'contextmenu'
                console.log(node.node.eventKey)
                this.rightClickSelectedKey = node.node.eventKey
            },
            onExpand(expandedKeys) {
                console.log('onExpand', expandedKeys);
                this.expandedKeys = expandedKeys;
                this.autoExpandParent = false;
            },
            switchCheckStrictly(v) {
                if(v==1){
                    this.checkStrictly = false
                }else if(v==2){
                    this.checkStrictly = true
                }
            },
            checkAll() {
                this.checkedKeys = this.allTreeKeys
            },
            cancelCheckAll() {
                this.checkedKeys = []
            },
            // 右键点击下拉框改变事件
            dropStatus(visible) {
                if (visible == false) {
                    this.dropTrigger = ''
                }
            },
        }
    }
</script>
<style scoped>

    .ant-tabs-card{
        background-color: #fff;
    }

    .ant-btn {
        margin-left: 3px
    }

    .drawer-tree-operator-button {
        bottom: 0;
        width: 100%;
        /*border-top: 1px solid #e8e8e8;*/
        padding: 10px 16px;
        text-align: left;
        left: 0;
        /*background: #fff;*/
        border-radius: 0 0 2px 2px;
    }

</style>
