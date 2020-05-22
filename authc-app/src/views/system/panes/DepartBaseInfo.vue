<template>
    <a-card :bordered="false">
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
                <a-tree-select
                        style="width:100%"
                        :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
                        :treeData="departTree"
                        v-model="model.parentId"
                        placeholder="请选择上级部门">
                </a-tree-select>
            </a-form-item>
            <a-form-item label="机构级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <z-dict-view dict-code="org_level" v-decorator="['orgLevel', validationRules.orgLevel]" :type="'list'" :triggerChange="true" :show-tips="true" placeholder="请选择机构级别"></z-dict-view>
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
        <div class="ant-form-btn">
            <a-button @click="resetForm" type="default" htmlType="button" icon="sync">重置</a-button>
            <a-button @click="submitForm" type="primary" htmlType="button" icon="form">修改并保存</a-button>
        </div>
    </a-card>
</template>
<script>
    import ZDictView from "../../../components/tools/ZDictView";
    import userAPI from "../../../api/services/user-api";
    import pick from "lodash.pick";

    export default{
        name:'DepartBaseInfo',
        components:{
          ZDictView
        },
        data() {
            return {
                form: this.$form.createForm(this),
                model:{},
                visibleCheck: true,
                status: 1,
                isTop: false,
                validationRules:{
                    departName:{rules:[{required: true, message: "请输入机构名称"}]},
                    orgCategory:{rules:[{required: true, message: "请选择机构类型"}]},
                    orgLevel:{rules:[{required: true, message: "请选择机构级别"}]},
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
        props:{
            departTree:{
                type: Array,
                required: true
            },
            currSelected:{
                type: Object,
                required: false
            }
        },
        watch:{
            currSelected(){
                console.log("currSelected",this.currSelected)
                this.visibleCheck = (this.currSelected.status == 1) ? true : false;
                this.form.resetFields()
                this.model = this.currSelected
                // this.model.orgLevel=parseInt(this.currSelected.orgLevel)
                this.$nextTick(()=>{
                    if(!this.currSelected.parentId){
                        this.isTop = true
                    }else{
                        this.isTop = false
                    }
                    this.form.setFieldsValue(pick(this.currSelected, 'departName','departNameAbbr','orgCategory','orgLevel', 'orgCode', 'departOrder','phone','mobile', 'fax', 'address', 'memo'))
                })
            }
        },
        methods:{
            resetForm(){
                this.form.resetFields()
            },
            submitForm(){
                let that = this
                this.form.validateFields((err, values) =>{
                    if(!err){
                        let paramData = Object.assign(this.model, values)
                        paramData.status = this.status
                        console.log("paramdata", paramData)
                        userAPI.editDepart(paramData).then((res)=>{
                            if(res.success){
                                that.$message.success(res.message)
                                this.$emit("reloadTree")
                            }
                        })
                    }
                })
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
    .ant-card-body .table-operator {
        margin: 15px;
    }

    .ant-form-btn{
        width: 100%;
        text-align: center;
    }
    .ant-btn {
        margin-left: 10px
    }
</style>
