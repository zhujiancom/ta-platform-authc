<template>
    <a-modal
            :title="title"
            :width="900"
            :visible="visible"
            :confirmLoading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
            :okButtonProps="{props: {disabled: disableSubmit}}"
            wrapClassName="ant-modal-cust-warp"
            style="top:5%;height: 85%;overflow-y: hidden"
            cancelText="关闭">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="用户账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入用户账号" v-decorator="['username', validationRules.username]"
                             :readOnly="disableSubmit"></a-input>
                </a-form-item>

                <template v-if="!userId">
                    <a-form-item label="登陆密码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input type="password" placeholder="请输入登陆密码"
                                 v-decorator="[ 'password', validationRules.password]"/>
                    </a-form-item>

                    <a-form-item label="确认密码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input type="password" @blur="handleConfirmBlur" placeholder="请重新输入登陆密码"
                                 v-decorator="[ 'confirmpassword', validationRules.confirmPassword]"/>
                    </a-form-item>
                </template>

                    <a-form-item label="用户姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input placeholder="请输入用户姓名" v-decorator="[ 'realname', validationRules.realname]"/>
                    </a-form-item>

                    <a-form-item label="工号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input placeholder="请输入工号" v-decorator="[ 'workNo', validationRules.workNo]"/>
                    </a-form-item>

                    <a-form-item label="角色分配" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="!roleDisabled" >
                        <a-select
                                mode="multiple"
                                style="width: 100%"
                                placeholder="请选择用户角色"
                                optionFilterProp="children"
                                v-model="selectedRole"
                        >
                            <a-select-option v-for="(role,roleIndex) in roleList" :key="roleIndex.toString()" :value="role.id">{{role.roleName}}</a-select-option>
                        </a-select>
                    </a-form-item>

                    <a-form-item label="身份" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-radio-group
                                v-model="identity"
                                @change="identityChange">
                            <a-radio value="1">普通用户</a-radio>
                            <a-radio value="2">上级</a-radio>
                        </a-radio-group>
                    </a-form-item>

                    <a-form-item label="头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <z-image-upload class="avatar-uploader" text="上传" v-model="fileList"></z-image-upload>
                    </a-form-item>

                    <a-form-item label="生日" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-date-picker
                                style="width: 100%"
                                placeholder="请选择生日"
                                v-decorator="['birthday', {initialValue:!model.birthday?null:moment(model.birthday,dateFormat)}]"/>
                    </a-form-item>

                    <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <z-dict-view v-decorator="['sex', {}]" placeholder="请选择性别" :type="'list'" dict-code="sex" :triggerChange="true" :show-tips="true"></z-dict-view>
                    </a-form-item>

                    <a-form-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input placeholder="请输入邮箱" v-decorator="[ 'email', validationRules.email]" />
                    </a-form-item>

                    <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input placeholder="请输入手机号码" :disabled="isDisabledAuth('user:form:phone')" v-decorator="[ 'phone', validationRules.phone]" />
                    </a-form-item>

                    <a-form-item label="座机" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input placeholder="请输入座机" v-decorator="[ 'telephone', validationRules.telephone]"/>
                    </a-form-item>

            </a-form>
        </a-spin>
    </a-modal>
</template>
<script>
    import moment from 'moment';
    import Vue from 'vue';
    import pick from "lodash.pick";
    import {ACCESS_TOKEN} from "../../../store/mutation-types";
    import UserAPI from "../../../api/services/user-api";
    import CommonAPI from "../../../api/services/common-api";
    import ZDictView from "../../../components/tools/ZDictView";
    import ZImageUpload from "../../../components/tools/ZImageUpload";
    import authorityAPI from "../../../api/services/authority-api";

    export default {
        name: 'UserModal',
        components: {ZImageUpload, ZDictView},
        data() {
            return {
                title: '操作',
                confirmLoading: false,
                visible: false,
                disableSubmit: false,
                form: this.$form.createForm(this),
                model: {},
                identity:"1",
                headers:{},
                departIdShow: false,
                userId:"",
                dateFormat:"YYYY-MM-DD",
                fileList:[],
                roleDisabled: false,
                roleList: [],
                selectedRole: [],
                labelCol: {
                    xs: {span: 24},
                    sm: {span: 6},
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 18}
                },
                validationRules: {
                    username: {
                        rules: [
                            {required: true, message: '请输入用户账号!'},
                            {validator: this.validateUsername}
                            ]
                    },
                    password: {
                        rules:[
                            {
                                required: true,
                                pattern:/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,./]).{8,}$/,
                                message: '密码由8位数字、大小写字母和特殊符号组成!'
                            },{
                            validator: this.validateToNextPassword
                            }
                        ]
                    },
                    confirmPassword:{
                        rules: [{
                            required: true, message: '请重新输入登陆密码!',
                        }, {
                            validator: this.compareToFirstPassword,
                        }],
                    },
                    realname:{rules: [{ required: true, message: '请输入用户名称!' }]},
                    phone:{rules: [{validator: this.validatePhone}]},
                    email:{
                        rules: [{
                            validator: this.validateEmail
                        }],
                    },
                    workNo: {
                        rules: [
                            { required: true, message: '请输入工号' },
                            { validator: this.validateWorkNo }
                        ]
                    },
                    telephone: {
                        rules: [
                            { pattern: /^0\d{2,3}-[1-9]\d{6,7}$/, message: '请输入正确的座机号码' },
                        ]
                    }
                }
            }
        },
        created () {
            const token = Vue.ls.get(ACCESS_TOKEN)
            this.headers = {"X-Access-Token":token}
        },
        watch:{
            model(){
                this.form.setFieldsValue(pick(this.model,'username','sex','realname','email','phone','workNo','telephone'))
                this.fileList = this.model.avatar
            }
        },
        methods: {
            moment,
            add () {
                this.refresh()
                this.edit({activitiSync:'1'});
            },
            edit(record){
                console.log("record = ", record)
                let that = this
                this.form.resetFields()
                this.initRoleList()
                if(Object.prototype.hasOwnProperty.call(record,"id")){
                    that.userId = record.id
                    that.loadUserInfo(record.id)
                }
                this.visible = true

                //TODO 调用查询用户对应的部门信息的方法
            },
            refresh () {
                this.userId=""
                this.model = {}
            },
            handleOk() {
                const that = this;
                that.form.validateFields((err, values) => {
                    if(!err){
                        that.confirmLoading = true;
                        if(!values.birthday){
                            values.birthday = '';
                        }else{
                            values.birthday = values.birthday.format(this.dateFormat)
                        }
                        let formData = Object.assign(this.model, values)
                        formData.identity=this.identity
                        formData.avatar = this.fileList
                        formData.selectedroles = this.selectedRole.length>0?this.selectedRole.join(","):''
                        let obj
                        if(!this.model.id){
                            formData.id = this.userId
                            obj = UserAPI.addUser(formData)
                        }else{
                            obj = UserAPI.editUser(formData)
                        }
                        obj.then((res)=>{
                            if(res.success){
                                that.$message.success(res.message)
                                that.$emit('ok')
                            }else{
                                that.$message.warning(res.message)
                            }
                        }).finally(()=>{
                            that.confirmLoading = false
                            //TODO
                            // that.checkedDepartNames = []
                            // that.userDepartModel.departIdList = {userId:'',departIdList:[]}
                            that.close()
                        })
                    }
                })
            },
            handleCancel() {
                this.close()
            },
            close(){
                this.visible = false;
                this.$emit('close')
                this.selectedRole = []
                this.disableSubmit = false
                this.fileList = []
                this.model = {}
            },
            handleConfirmBlur() {

            },
            loadUserInfo(id){
                let that = this
                UserAPI.queryUserInfo({id:id}).then((res)=>{
                    if(res.success){
                        console.log("result model = ", res.data.model)
                        that.model = res.data.model
                        that.selectedRole = res.data.selectedRole
                        that.$message.success(res.message)
                    }else{
                        that.$message.warning(res.message)
                    }
                })
            },
            initRoleList(){
                console.log("---- loading role list")
                let that = this
                authorityAPI.queryAllRoles().then((res)=>{
                    if(res.success){
                        this.roleList = res.data;
                    }else{
                        that.$message.warning(res.message)
                    }
                })
            },
            // loadUserRoles(userId){
            //     console.log("----- load user["+userId+"] roles")
            //     let that = this
            //     authorityAPI.queryUserRole({userid:userId}).then((res)=>{
            //         if(res.success){
            //             this.selectedRole = res.result
            //         }else{
            //             that.$message.warning(res.message)
            //         }
            //     })
            // },
            validateUsername(rule, value, callback){
                let params = {
                    tableName: 't_sys_user',
                    fieldName: 'username',
                    fieldVal: value,
                    dataId: this.userId
                };
                CommonAPI.duplicateCheck(params).then((res) => {
                    if (res.success) {
                        callback()
                    } else {
                        callback("用户名已存在!")
                    }
                })
            },
            validateToNextPassword  (rule, value, callback) {
                const form = this.form;
                const confirmPassword=form.getFieldValue('confirmpassword');

                if (value && confirmPassword && value !== confirmPassword) {
                    callback('两次输入的密码不一样！');
                }
                if (value && this.confirmDirty) {
                    form.validateFields(['confirm'], { force: true })
                }
                callback();
            },
            validateWorkNo(rule, value, callback){
                let params = {
                    tableName: 't_sys_user',
                    fieldName: 'work_no',
                    fieldVal: value,
                    dataId: this.userId
                };
                CommonAPI.duplicateCheck(params).then((res) => {
                    if (res.success) {
                        callback()
                    } else {
                        callback("工号已存在!")
                    }
                })
            },
            validatePhone(rule, value, callback){
                if(!value){
                    callback()
                }else{
                    if(new RegExp(/^1[3|4|5|7|8|9][0-9]\d{8}$/).test(value)){
                        let params = {
                            tableName: 't_sys_user',
                            fieldName: 'phone',
                            fieldVal: value,
                            dataId: this.userId
                        };
                        CommonAPI.duplicateCheck(params).then((res) => {
                            if (res.success) {
                                callback()
                            } else {
                                callback("手机号已存在!")
                            }
                        })
                    }else{
                        callback("请输入正确格式的手机号码!");
                    }
                }
            },
            validateEmail(rule, value, callback){
                if(!value){
                    callback()
                }else{
                    // eslint-disable-next-line no-useless-escape
                    if(new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/).test(value)){
                        let params = {
                            tableName: 't_sys_user',
                            fieldName: 'email',
                            fieldVal: value,
                            dataId: this.userId
                        };
                        CommonAPI.duplicateCheck(params).then((res) => {
                            console.log(res)
                            if (res.success) {
                                callback()
                            } else {
                                callback("邮箱已存在!")
                            }
                        })
                    }else{
                        callback("请输入正确格式的邮箱!")
                    }
                }
            },
            isDisabledAuth(){

            },
            identityChange(e){
                if(e.target.value==="1"){
                    this.departIdShow=false;
                }else{
                    this.departIdShow=true;
                }
            },
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
