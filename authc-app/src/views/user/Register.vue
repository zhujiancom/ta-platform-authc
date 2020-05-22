<template>
    <div class="main user-layout-register">
        <h3><span>注册</span></h3>
        <a-form ref="formRegister" :form="form" id="formRegister">
            <a-form-item>
                <a-input v-decorator="[
                    'username',
                    {
                        rules: [{ required: true, message: '用户名不能为空'}, {validator: this.checkUsername}],
                        validateTrigger: ['change', 'blur']
                    }
                ]" size="large" type="text" autocomplete="false" placeholder="请输入用户名"></a-input>
            </a-form-item>

            <a-form-item>
                <a-button
                        size="large"
                        type="primary"
                        htmlType="submit"
                        class="register-button"
                        :loading="registerBtn"
                        @click.stop.prevent="handleSubmit"
                        :disabled="registerBtn">注册
                </a-button>
                <router-link class="login" :to="{ name: 'login' }">使用已有账户登录</router-link>
            </a-form-item>
        </a-form>
    </div>
</template>

<script>
    import {mixinDevice} from "../../utils/mixin";
    import {postAction} from "../../api/http";

    const levelNames = {
        0: '低',
        1: '低',
        2: '中',
        3: '强'
    }
    const levelClass = {
        0: 'error',
        1: 'error',
        2: 'warning',
        3: 'success'
    }
    const levelColor = {
        0: '#ff0000',
        1: '#ff0000',
        2: '#ff7e05',
        3: '#52c41a',
    }
    export default {
        name: 'Register',
        components: {},
        mixins: [mixinDevice],
        data(){
            return {
                form: null,
                state: {
                    time: 60,
                    smsSendBtn: false,
                    passwordLevel: 0,
                    passwordLevelChecked: false,
                    percent: 10,
                    progressColor: '#FF0000'
                },
                registerBtn: false
            }
        },
        computed: {
            passwordLevelClass(){
                return levelClass[this.state.passwordLevel]
            },
            passwordLevelName(){
                return levelNames[this.state.passwordLevel]
            },
            passwordLevelColor(){
                return levelColor[this.state.passwordLevel]
            }
        },
        methods: {
            checkUsername(rule, value, callback){
                var params={
                    username: value,
                };
                this.$api.userAPI.checkOnlyUser(params).then((res) => {
                    if(res.success){
                        callback()
                    }else{
                        callback("用户名已存在")
                    }
                })
            },
            handleSubmit() {
                this.form.validateFields((err, values) => {
                    if (!err) {
                        var register = {
                            username: values.username,
                            password: values.password,
                            email: values.email,
                            phone: values.mobile,
                            smscode: values.captcha
                        };
                        postAction("/sys/user/register", register).then((res) => {
                            if (!res.success) {
                                this.registerFailed(res.message)
                            } else {
                                this.$router.push({name: 'registerResult', params: {...values}})
                            }
                        })
                    }
                })
            },
        },

    }
</script>

<style lang="less" scoped>
    .user-layout-register {

    & > h3 {
          font-size: 16px;
          margin-bottom: 20px;
      }

    .getCaptcha {
        display: block;
        width: 100%;
        height: 40px;
    }

    .register-button {
        width: 50%;
    }

    .login {
        float: right;
        line-height: 40px;
    }
    }
</style>
