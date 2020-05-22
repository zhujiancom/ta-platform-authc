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
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="角色名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入角色名称" v-decorator="['roleName', validationRules.roleName]"></a-input>
                </a-form-item>
                <a-form-item label="角色编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入角色编码" v-decorator="['roleCode', validationRules.roleCode]"
                             :disabled="readOnly"></a-input>
                </a-form-item>
                <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea placeholder="描述..." :rows="5"
                                v-decorator="['description', validationRules.description]"></a-textarea>
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
</template>
<script>
    import pick from "lodash.pick";
    import authorityAPI from "../../../api/services/authority-api";
    import commonAPI from "../../../api/services/common-api";

    export default {
        name: 'RoleModal',
        data() {
            return {
                title: "操作",
                confirmLoading: false,
                readOnly: false,
                visible: false,
                form: this.$form.createForm(this),
                model: {},
                labelCol: {
                    xs: {span: 24},
                    sm: {span: 5},
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 16},
                },
                validationRules: {
                    roleName: {
                        rules: [
                            {required: true, message: '请输入角色名称!'},
                            {min: 2, max: 30, message: '长度在 2 到 30 个字符'}
                        ]
                    },
                    roleCode: {
                        rules: [
                            {required: true, message: '请输入角色名称!'},
                            {min: 0, max: 64, message: '长度不超过 64 个字符'},
                            {validator: this.validateRoleCode}
                        ]
                    },
                    description: {
                        rules: [
                            {min: 0, max: 126, message: '长度不超过 126 个字符'}
                        ]
                    }
                },
            }
        },
        methods: {
            add() {
                this.edit({})
            },
            edit(record) {
                this.visible = true
                this.form.resetFields()
                if (record.id) {
                    this.readOnly = true
                } else {
                    this.readOnly = false
                }
                this.model = Object.assign({}, record)
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'roleName', 'roleCode', 'description'))
                });
            },
            handleOk(e) {
                e.preventDefault();
                let that = this
                this.form.validateFields((err, values) => {
                    if (!err) {
                        that.confirmLoading = true;
                        let obj
                        let formData = Object.assign(this.model, values)
                        if (!this.model.id) {
                            obj = authorityAPI.addRole(formData)
                        } else {
                            obj = authorityAPI.editRole(formData)
                        }
                        obj.then((res) => {
                            if (res.success) {
                                that.$message.success(res.message)
                                that.$emit('ok')
                            } else {
                                that.$message.warning(res.message)
                            }
                        }).finally(() => {
                            that.confirmLoading = false
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
                this.visible = false;
            },
            validateRoleCode(rule, value, callback) {
                if (/[\u4E00-\u9FA5]/g.test(value)) {
                    callback("角色编码不可输入汉字!");
                }else{
                    var params = {
                        tableName: "t_sys_role",
                        fieldName: "role_code",
                        fieldVal: value,
                        dataId: this.model.id,
                    };
                    commonAPI.duplicateCheck(params).then((res)=>{
                        if(res.success){
                            callback();
                        }else{
                            callback(res.message);
                        }
                    });
                }
            }
        }
    }
</script>
