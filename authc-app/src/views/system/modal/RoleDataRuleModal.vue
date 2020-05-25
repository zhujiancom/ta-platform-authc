<template>
    <a-drawer title="数据规则配置"
              width="365"
              :closable="false"
              @close="onClose"
              :visible="visible">
        <a-tabs defaultActiveKey="1">
            <a-tab-pane tab="数据规则" key="1">

                <a-checkbox-group v-model="dataRuleChecked" v-if="dataRuleList.length>0">
                    <a-row>
                        <a-col :span="24" v-for="(item,index) in dataRuleList" :key=" 'dr'+index ">
                            <a-checkbox :value="item.id">{{ item.ruleName }}</a-checkbox>
                        </a-col>

                        <a-col :span="24">
                            <div style="width: 100%;margin-top: 15px">
                                <a-button @click="saveDataRuleForRole" type="primary" size="small" icon="save">点击保存</a-button>
                            </div>
                        </a-col>
                    </a-row>
                </a-checkbox-group>
                <div v-else><h3>无配置信息!</h3></div>

            </a-tab-pane>
        </a-tabs>
    </a-drawer>
</template>
<script>
    export default {
        name:'RoleDataRuleModal',
        data() {
            return {
                roleId:'',
                visible: false,
                dataRuleChecked:[],
                dataRuleList:[],
                activeTabKey: '1',
            }
        },
        methods:{
            onClose(){
                this.visible=false
                this.onReset()
            },
            saveDataRuleForRole(){

            },
            onReset(){
                this.routeId=''
                this.dataruleList=[]
                this.dataruleChecked=[]
            }
        }
    }
</script>
