<template>
    <a-drawer :title="title"
        :maskClosable="true"
              width="650"
              placement="right"
              :closable="true"
              @close="close"
              :visible="visible"
              :wrapStyle="{height: 'calc(100% - 55px)',overflow: 'auto',paddingBottom: '53px'}"
    >
        <a-form>
            <a-form-item label="所拥有的权限">
                <a-tree
                    checkable
                    @check="onCheck"
                    :checkedKeys="checkedKeys"
                    :treeData="treeData"
                    @expand="onExpand"
                    @select="onTreeNodeSelect"
                    :selectedKeys="selectedKeys"
                    :expendedKeys="expandedKeys"
                    :checkStrictly="checkStrictly"
                >
                    <span slot="hasDataRule" slot-scope="{slotTitle, ruleFlag}">
                        {{slotTitle}}<a-icon v-if="ruleFlag" type="align-left" style="margin-left:5px;color: red;"></a-icon>
                    </span>
                </a-tree>
            </a-form-item>
        </a-form>

        <div class="drawer-bottom-button">
            <a-dropdown style="float: left" :trigger="['click']" placement="topCenter">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="switchCheckStrictly(1)">父子关联</a-menu-item>
                    <a-menu-item key="2" @click="switchCheckStrictly(2)">取消关联</a-menu-item>
                    <a-menu-item key="3" @click="checkALL">全部勾选</a-menu-item>
                    <a-menu-item key="4" @click="cancelCheckALL">取消全选</a-menu-item>
                    <a-menu-item key="5" @click="expandAll">展开所有</a-menu-item>
                    <a-menu-item key="6" @click="closeAll">合并所有</a-menu-item>
                </a-menu>
                <a-button>
                    树操作 <a-icon type="up" />
                </a-button>
            </a-dropdown>
            <a-popconfirm title="确定放弃编辑？" @confirm="close" okText="确定" cancelText="取消">
                <a-button style="margin-right: .8rem">取消</a-button>
            </a-popconfirm>
            <a-button @click="handleSubmit(false)" type="primary" :loading="loading" ghost style="margin-right: 0.8rem">仅保存</a-button>
            <a-button @click="handleSubmit(true)" type="primary" :loading="loading">保存并关闭</a-button>
        </div>

    </a-drawer>
</template>
<script>
    export default {
        name:'RolePermissionModal',
        data() {
            return {
                title: '角色权限配置',
                visible: false,
                treeData:[],
                checkedKeys:[],
                selectedKeys:[],
                expandedKeys:[],
                checkStrictly: true
            }
        },
        methods:{
            close(){

            },
            onCheck(){

            },
            onExpand(){

            },
            onTreeNodeSelect(){

            },
            handleSubmit(closeable){
                console.log(closeable)
            }
        }
    }
</script>
<style lang="less" scoped>
    .drawer-bottom-button {
        position: absolute;
        bottom: 0;
        width: 100%;
        border-top: 1px solid #e8e8e8;
        padding: 10px 16px;
        text-align: right;
        left: 0;
        background: #fff;
        border-radius: 0 0 2px 2px;
    }

</style>
