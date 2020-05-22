<template>
    <a-modal :title="title"
             :width="600"
             :visible="visible"
             :confirmLoading="confirmLoading"
             @ok="handleOk"
             @cancel="handleCancel"
             cancelText="关闭">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="名称">
                    <a-input v-decorator="['itemText',validationRules.itemText]"></a-input>
                </a-form-item>
                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="数据值">
                    <a-input v-decorator="['itemValue',validationRules.itemValue]"></a-input>
                </a-form-item>
                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="描述">
                    <a-textarea v-decorator="['description', {}]"></a-textarea>
                </a-form-item>
                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="排序值">
                    <a-input-number :min="1" v-decorator="['sortOrder',{'initialValue':1}]"/>值越小越靠前，支持小数
                </a-form-item>
                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="是否启用"
                        hasFeedback>
                    <a-switch checkedChildren="启用" unCheckedChildren="禁用" @change="onChose" v-model="visibleCheck"/>
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
</template>
<script>
    import pick from "lodash.pick";
    import commonAPI from "../../../api/services/common-api";

    export default {
        name:'SysDictItemModal',
        data() {
            return {
                title: '操作',
                form: this.$form.createForm(this),
                confirmLoading: false,
                visible: false,
                visibleCheck: true,
                status: 1,
                model:{},
                labelCol: {
                    xs: {span: 24},
                    sm: {span: 5},
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 16},
                },
                validationRules:{
                    itemText:{
                        rules:[
                            {required: true, message: '请填写名称！'}
                        ]
                    },
                    itemValue:{
                        rules:[
                            {required: true, message: '请填写数据值！'}
                        ]
                    }
                }
            }
        },
        props:["dictId"],
        methods:{
            add(){
                console.log("dictId = ", this.dictId)
                this.edit({dictId: this.dictId})
            },
            edit(record){
                this.visible = true
                console.log("dict Item record = ", record)
                if(record.id){
                    this.visibleCheck = (record.status == 1) ? true : false;
                }
                this.form.resetFields()
                this.model = Object.assign({}, record);
                this.$nextTick(()=>{
                    this.form.setFieldsValue(pick(this.model,'itemText','itemValue','description','sortOrder'))
                })
            },
            handleOk(){
                let that = this
                this.form.validateFields((err, values)=>{
                    if(!err){
                        this.confirmLoading = true
                        values.itemText = (values.itemText || '').trim()
                        values.itemValue = (values.itemValue || '').trim()
                        values.description = (values.description || '').trim()
                        let formData = Object.assign(this.model, values)
                        formData.status = this.status
                        let obj
                        if(!this.model.id){
                            obj = commonAPI.addDictItem(formData)
                        }else{
                            obj = commonAPI.editDictItem(formData)
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
                            that.close()
                        })
                    }
                })
            },
            handleCancel(){
                this.close()
            },
            close(){
                this.visible = false
                this.mode = {}
                this.$emit('close')
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
