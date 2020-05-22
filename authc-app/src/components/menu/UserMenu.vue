<template>
    <div class="user-wrapper" :class="theme">
        <span>
            <a-icon type="search"></a-icon>
        </span>

        <header-notice class="action"/>
        <a-dropdown>
              <span class="action action-full ant-dropdown-link user-dropdown-menu">
                <a-avatar class="avatar" size="small" :src="getAvatar()"/>
                <span v-if="isDesktop()">欢迎您，{{ nickname() }}</span>
              </span>
            <a-menu slot="overlay" class="user-dropdown-menu-wrapper">
                <a-menu-item key="0">
                    <router-link :to="{ name: 'account-center' }">
                        <a-icon type="user"/>
                        <span>个人中心</span>
                    </router-link>
                </a-menu-item>
                <a-menu-item key="1">
                    <router-link :to="{ name: 'account-settings-base' }">
                        <a-icon type="setting"/>
                        <span>账户设置</span>
                    </router-link>
                </a-menu-item>
                <a-menu-item key="3" @click="systemSetting">
                    <a-icon type="tool"/>
                    <span>系统设置</span>
                </a-menu-item>
                <a-menu-item key="4" @click="updatePassword">
                    <a-icon type="lock"/>
                    <span>密码修改</span>
                </a-menu-item>
                <a-menu-item key="5" @click="updateCurrentDepart">
                    <a-icon type="cluster"/>
                    <span>切换部门</span>
                </a-menu-item>
            </a-menu>
        </a-dropdown>
        <span class="action">
          <a class="logout-title" href="javascript:;" @click="handleLogout">
            <a-icon type="logout"/>
            <span v-if="isDesktop()">&nbsp;退出登录</span>
          </a>
        </span>
        <user-password ref="userPassword"></user-password>
    </div>
</template>

<script>
    import {mixinDevice} from "../../utils/mixin";
    import {mapActions, mapGetters} from "vuex";
    import UserPassword from "../tools/UserPassword";
    import HeaderNotice from "../tools/notice/HeaderNotice";

    export default {
        name: 'user-menu',
        mixins: [mixinDevice],
        props: {
            theme: {
                type: String,
                required: false,
                default: 'dark'
            }
        },
        components: {
            HeaderNotice,
            UserPassword
        },
        methods: {
            ...mapActions(['Logout']),
            ...mapGetters(['nickname', 'avatar', 'userInfo']),
            handleLogout() {
                const that = this
                this.$confirm({
                    title: '提示',
                    content: '确定要注销登录吗？',
                    onOk() {
                        return that.Logout({}).then(() => {
                            window.location.href = "/";
                        }).catch(err => {
                            that.$message.error({
                                title: '错误',
                                description: err.message
                            })
                        })
                    },
                    onCancel() {
                    },
                });
            },
            getAvatar() {
                console.log('url = ' + window._CONFIG['staticDomainURL'] + "/" + this.avatar())
                return window._CONFIG['staticDomainURL'] + "/" + this.avatar()
            },
            updatePassword() {
                let username = this.userInfo().username
                this.$refs.userPassword.show(username)
            },
            systemSetting() {
                this.$refs.settingDrawer.showDrawer()
            },
            updateCurrentDepart() {
                this.$refs.departSelect.show()
            },
        }
    }
</script>

<style lang="less" scoped>
    .user-wrapper .search-input {
        width: 180px;
        color: inherit;

        /deep/ .ant-select-selection {
            background-color: inherit;
            border: 0;
            border-bottom: 1px solid white;

            &__placeholder, &__field__placeholder {
                color: inherit;
            }
        }
    }

    .logout-title {
        color: inherit;
        text-decoration: none;
    }
</style>
