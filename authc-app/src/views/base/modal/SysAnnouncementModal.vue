<template>
    <a-modal
        :title="title"
        :width="900"
        :visible="visible"
        :confirmLoading="confirmLoading"
        @ok="handleOk"
        @cancel="handleCancel"
        :okButtonProps="{props: {disabled: disableSubmit}}"
        cancelText="关闭"
    >
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-row style="width: 100%">
                    <a-col :span="12">
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标题:">
                            <a-input placeholder="请输入标题" v-decorator="['title',validationRules.title]" :readOnly="disableSubmit"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="消息类型:">
<!--                            <a-select-->
<!--                                    v-decorator="[ 'category', validationRules.category]"-->
<!--                                    placeholder="请选择消息类型"-->
<!--                                    :disabled="disableSubmit"-->
<!--                                    :getPopupContainer = "(target) => target.parentNode">-->
<!--                                <a-select-option v-for="item in categoryOptions" :key="item.value" :value="item.value">{{item.label}}</a-select-option>-->
<!--                            </a-select>-->
                            <z-dict-view v-decorator="['category', validationRules.category]" placeholder="请选择消息类型" dict-code="notice_type" :type="'list'" :triggerChange="true"></z-dict-view>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row style="width: 100%">
                    <a-col :span="12">
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开始时间:">
                            <z-date style="width: 100%" :getCalendarContainer="node => node.parentNode" v-decorator="[ 'startTime', validationRules.startTime]" placeholder="请选择开始时间" showTime dateFormat="YYYY-MM-DD HH:mm:ss"></z-date>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="结束时间：">
                            <z-date style="width: 100%" :getCalendarContainer="node => node.parentNode" v-decorator="[ 'endTime', validationRules.endTime]" placeholder="请选择结束时间" showTime dateFormat="YYYY-MM-DD HH:mm:ss"></z-date>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row style="width: 100%">
                    <a-col :span="12">
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="优先级">
                            <z-dict-view v-decorator="['priority', {}]" placeholder="请选择优先级" :show-tips="true" dict-code="priority" :type="'list'" :triggerChange="true"></z-dict-view>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="通告对象">
<!--                            <a-select-->
<!--                                    v-decorator="[ 'receiverType', validationRules.receiverType]"-->
<!--                                    placeholder="请选择通告对象"-->
<!--                                    :disabled="disableSubmit"-->
<!--                                    :getPopupContainer = "(target) => target.parentNode">-->
<!--                                <a-select-option v-for="item in receiverTypeOptions" :key="item.value" :value="item.value">{{item.label}}</a-select-option>-->
<!--                            </a-select>-->
                            <z-dict-view v-decorator="['receiverType', validationRules.receiverType]" placeholder="请选择通告对象" dict-code="receiver_type" :type="'list'" :triggerChange="true"></z-dict-view>
<!--                            <z-dict-view2 v-decorator="['receiverType', validationRules.receiverType]" placeholder="请选择通告对象" category="receiver_type"></z-dict-view2>-->
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row style="width: 100%">
                    <a-col :span="24">
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="指定用户" v-if="specUser">
                            <a-select
                                    mode="multiple"
                                    placeholder="请选择用户"
                                    v-model="selectedUser"
                                    @dropdownVisibleChange="selectUserIds"
                            ></a-select>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row style="width: 100%">
                    <a-col :span="24">
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="指定用户" v-if="specRole">
                            <a-select
                                    mode="multiple"
                                    placeholder="请选择角色"
                                    v-model="selectedRole"
                                    @dropdownVisibleChange="selectRoleIds"
                            ></a-select>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row style="width: 100%;">
                    <a-col :span="24">
                        <a-form-item
                                :labelCol="labelColX1"
                                :wrapperCol="wrapperColX1"
                                label="内容">
                            <z-editor v-decorator="[ 'content', {} ]" triggerChange></z-editor>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
        </a-spin>
    </a-modal>
</template>
<script>
    import moment from 'moment'
    import ZDate from "../../../components/tools/ZDate";
    import ZEditor from "../../../components/tools/ZEditor";
    import {getAction, httpAction} from "../../../api/http";
    import pick from 'lodash.pick'
    import ZDictView from "../../../components/tools/ZDictView";

    export default {
        name: 'SysAnnouncementModal',
        components: { ZDictView, ZDate, ZEditor},
        data() {
            return {
                title: '操作',
                visible: false,
                confirmLoading: false,
                disableSubmit: false,
                form: this.$form.createForm(this),
                model:{},
                labelCol: {
                    xs: {span: 24},
                    sm: {span: 6},
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 18}
                },
                labelColX1: {
                    xs: { span: 24 },
                    sm: { span: 3 },
                },
                wrapperColX1: {
                    xs: { span: 24 },
                    sm: { span: 21 },
                },
                validationRules:{
                    title:{rules: [{ required: true, message: '请输入标题!' }]},
                    category:{rules: [{ required: true, message: '请选择消息类型!' }]},
                    receiverType:{rules: [{ required: true, message: '请选择通告对象类型!' }]},
                    endTime:{rules:[{validator: this.endTimeValidate}]},
                    startTime:{rules:[{validator: this.startTimeValidate}]}
                },
                url:{
                    add:'/core/notice/add',
                    edit:'/core/notice/edit',
                    queryRoleByIds:'/sys/role/query-role-by-ids',
                    queryUserByIds:'/sys/user/query-user-by-ids',
                },
                specUser: false,
                specRole: false,
                selectedUser:[],
                selectedRole:[],
            }
        },
        methods:{
            add () {
                this.form.resetFields()
                this.model = {}
                this.visible = true
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model,'endTime','startTime','title','content','publisher','priority','category','receiverType'))
                })
            },
            edit (record) {
                console.log("record = ",record)
                this.form.resetFields();
                this.model = Object.assign({}, record)
                this.visible = true
                if(record && record.receiverType === 'USER'){
                    this.getUser(record);
                }else if(record && record.receiverType === 'ROLE'){
                    this.getRole(record)
                }
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model,'endTime','startTime','title','content','category','publisher','priority','receiverType'))
                });
            },
            getUser(record){
                this.specUser = true
                this.userIds = record.userIds
                getAction(this.url.queryUserByIds, {userIds:this.userIds}).then((res) => {
                    if(res.success){
                        for(let i = 0;i<res.data.length; i++){
                            this.selectedUser.push(res.data[i].realname)
                        }
                        // TODO 打开已选择用户模态框
                        // this.$refs.UserListModal.edit(res.result, this.userIds)
                    }
                })
            },
            getRole(record){
                this.specRole = true
                this.roleIds = record.roleIds
                getAction(this.url.queryRoleByIds, {roleIds:this.roleIds}).then((res) => {
                    if(res.success){
                        for(let i = 0;i<res.data.length; i++){
                            this.selectedRole.push(res.data[i].roleName)
                        }
                        // TODO 打开已选择用户模态框
                        // this.$refs.RoleListModal.edit(res.result, this.roleIds)
                    }
                })
            },
            startTimeValidate(rule, value, callback){
                let endTime = this.form.getFieldValue('endTime')
                if(!value || !endTime){
                    callback()
                }else if(moment(endTime).isAfter(value)){
                    callback()
                }else{
                    callback("开始时间需小于结束时间")
                }
            },
            endTimeValidate(rule,value, callback){
                let startTime = this.form.getFieldValue('startTime')
                if(!value || !startTime){
                    callback()
                }else if(moment(startTime).isBefore(value)){
                    callback()
                }else{
                    callback("结束时间需大于开始时间")
                }
            },
            close(){
                this.$emit('close')
                this.selectedUser = []
                this.selectedRole = []
                this.visible = false;
            },
            handleOk(){
                const that = this
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if(!err){
                        that.confirmLoading = true;
                        let httpUrl = ''
                        let method = ''
                        // 新增 TODO 使用接口方法调用
                        if(!this.model.id){
                            httpUrl+=this.url.add
                            method = 'post'
                        }else{
                            // 修改
                            httpUrl += this.url.edit
                            method = 'put'
                        }
                        let formData = Object.assign(this.model, values)
                        if(this.specUser){
                            formData.userIds = this.userIds
                        }
                        console.log("formData = ", formData);
                        httpAction(httpUrl, formData, method).then((res) => {
                            if(res.success){
                                that.$message.success(res.message)
                                that.$emit('ok')
                            }else{
                                that.$message.warning(res.message)
                            }
                        }).finally(() => {
                            that.confirmLoading = false
                            that.close()
                        })
                    }
                })
            },
            handleCancel(){
                this.visible = false;
                this.$emit('close');
                // this.resetUser();
            },
            handleChange(val){
                console.log(val)
            },
            selectUserIds(){

            },
            selectRoleIds(){

            }
        }
    }
</script>
