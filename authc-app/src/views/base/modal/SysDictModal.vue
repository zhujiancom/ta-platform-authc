<template>
    <a-modal :title="title"
             :width="600"
             :visible="visible"
             :confirmLoading="confirmLoading"
             @ok="handleOk"
             @cancel="handleCancel"
             :okButtonProps="{props: {disabled: disableSubmit}}"
             cancelText="关闭">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="字典名称">
                    <a-input v-decorator="['dictName', validationRules.dictName]"/>
                </a-form-item>
                <a-form-item label="字典编码">
                    <a-input v-decorator="['dictCode', validationRules.dictCode]"/>
                </a-form-item>
                <a-form-item label="字典类型">
                    <a-select v-decorator="['type',{}]" placeholder="请选择字典类型" :getPopupContainer="(target) => target.parentNode">
                        <a-select-option value="0">文本型</a-select-option>
                        <a-select-option value="1">数值型</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="描述">
                    <a-textarea v-decorator="['description', {}]"></a-textarea>
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>

</template>
<script>
    import pick from "lodash.pick";
    import commonAPI from "../../../api/services/common-api";

    export default {
        name: 'SysDictModal',
        components: {},
        data() {
            return {
                title: '操作',
                form: this.$form.createForm(this),
                confirmLoading: false,
                disableSubmit: false,
                visible: false,
                model: {},
                validationRules: {
                    dictName: {rules: [{required: true, message: '请输入字典名称!'}]},
                    dictCode: {rules: [{required: true, message: '请输入字典编码！'},{validator: this.validateDictCode}]}
                }
            }
        },
        methods:{
            add(){
                this.edit({})
            },
            edit(record){
              this.form.resetFields()
              this.visible = true
                Object.assign(this.model, record)
                this.$nextTick(()=>{
                    this.form.setFieldsValue(pick(this.model,'dictName','dictCode','type','description'))
                })
            },
            handleOk(){
                let that = this
                that.form.validateFields((err, values)=>{
                    if(!err){
                        that.confirmLoading = true;
                        let formData = Object.assign(this.model, values)
                        let obj
                        if(!this.model.id){
                            formData.id = this.userId
                            obj = commonAPI.addDict(formData)
                        }else{
                            obj = commonAPI.editDict(formData)
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
                this.model = {}
                this.$emit('close')
            },
            validateDictCode(rule, value, callback){
                let params = {
                    tableName: 't_sys_dict',
                    fieldName: 'dict_code',
                    fieldVal: value,
                    dataId: this.model.id
                }
                commonAPI.duplicateCheck(params).then((res) => {
                    if (res.success) {
                        callback()
                    } else {
                        callback(res.message)
                    }
                })
            }
        }
    }
</script>
