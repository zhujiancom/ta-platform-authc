<template>
    <a-drawer :title="title"
              :width="drawerWidth"
              @close="onClose"
              :visible="visible"
              :wrapStyle="{height: 'calc(100% - 108px)',overflow: 'auto',paddingBottom: '108px'}"></a-drawer>
</template>
<script>
    export default {
        name: 'PermissionDataRule',
        data() {
            return {
                title: '数据权限规则配置',
                visible: false,
                drawerWidth: 650,
            }
        },
        methods:{
            edit(record){
                console.log(record)
            },
            onClose(){

            }
        }
    }
</script>
