<template>
    <a-modal
            :title="title"
            :width="800"
            :visible="visible"
            :confirm-loading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
            cancelText="关闭"
            wrapClassName="ant-modal-cust-warp"
            style="top:5%;height: 85%;overflow-y: hidden"
    >
        <a-form :form="form">
            <a-form-item label="机构名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['departName', validationRules.departName]" placeholder="请输入机构名称"></a-input>
            </a-form-item>
            <a-form-item label="机构缩写" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['departNameAbbr',{}]" placeholder="请输入公司缩写"></a-input>
            </a-form-item>
            <a-form-item label="机构类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <z-dict-view dict-code="org_category" v-decorator="['orgCategory', validationRules.orgCategory]" :type="'radio'" :triggerChange="true"></z-dict-view>
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :hidden="isTop" label="上级部门" hasFeedback>
                <a-input :value="parentName" :disabled="true"></a-input>
            </a-form-item>
            <a-form-item label="机构级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <z-dict-view dict-code="org_level" v-decorator="['orgLevel', validationRules.orgCategory]" :type="'list'" :triggerChange="true" :show-tips="true" placeholder="请选择机构级别"></z-dict-view>
            </a-form-item>
            <a-form-item label="机构编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['orgCode',{}]" placeholder="请输入公司编码"></a-input>
            </a-form-item>
            <a-form-item label="电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['phone',{}]" placeholder="请输入电话号码"></a-input>
            </a-form-item>
            <a-form-item label="手机" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['mobile',validationRules.mobile]" placeholder="请输入手机号码"></a-input>
            </a-form-item>
            <a-form-item label="传真" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['fax',{}]" placeholder="请输入传真"></a-input>
            </a-form-item>
            <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['address',{}]" placeholder="请输入地址"></a-input>
            </a-form-item>
            <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="是否启用"
                    hasFeedback>
                <a-switch checkedChildren="启用" unCheckedChildren="禁用" @change="onChose" v-model="visibleCheck"/>
            </a-form-item>
            <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="排序">
                <a-input-number :min="1" v-decorator="['departOrder',{'initialValue':1}]"/>
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                <a-textarea v-decorator="['memo',{}]" placeholder="备注信息" :rows="5"></a-textarea>
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
    import userAPI from "../../../api/services/user-api";
    import ZDictView from "../../../components/tools/ZDictView";

    export default {
        name:'DepartModal',
        components: {ZDictView},
        data() {
            return {
                title: '操作',
                form: this.$form.createForm(this),
                confirmLoading: false,
                visible: false,
                status: 1,
                visibleCheck: true,
                model: {},
                parentName: '',
                isTop: true,
                validationRules:{
                    departName:{rules:[{required: true, message: "请输入机构名称"}]},
                    orgCategory:{rules:[{required: true, message: "请选择机构类型"}]},
                    mobile:{rules: [{validator:this.validateMobile}]},
                    orgCode:{rules: [{ required: true, message: '请输入机构编码!' }]},
                },
                labelCol:{
                    xs: { span: 24 },
                    sm: { span: 5 },
                },
                wrapperCol:{
                    xs: { span: 24 },
                    sm: { span: 16 },
                }
            }
        },
        methods:{
            add(parentNode){
                console.log("parentNode", parentNode)
                this.visible = true
                if(parentNode){
                    this.isTop = false
                    this.parentName = parentNode.departName
                    this.model.parentId = parentNode.id
                }
                this.form.resetFields()
            },
            handleOk(){
                let that = this
                this.form.validateFields((err, values)=>{
                    if(!err){
                        that.confirmLoading = true;
                        let formData = Object.assign(this.model, values)
                        formData.status = this.status
                        userAPI.addDepart(formData).then((res)=>{
                            if(res.success){
                                that.$message.success(res.message)
                            }else{
                                that.$message.warning(res.message)
                            }
                        }).finally(() => {
                            that.confirmLoading = false
                            that.$emit('ok')
                            that.close()
                        })
                    }
                })
            },
            handleCancel() {
                this.close()
            },
            close() {
                this.$emit('close')
                this.visible = false
                this.isTop = true
            },
            validateMobile(rule,value,callback){
                if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)){
                    callback();
                }else{
                    callback("您的手机号码格式不正确!");
                }
            },
            onChose(checked){
                if (checked) {
                    this.status = 1;
                    this.visibleCheck = true;
                } else {
                    this.status = 0;
                    this.visibleCheck = false;
                }
            }
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
