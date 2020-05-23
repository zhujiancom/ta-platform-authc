<template>
    <div id="app">
        <div v-if="!network">
            <h3>请检查网络连接</h3>
            <div @click="onRefresh">刷新</div>
        </div>
        <router-view></router-view>
    </div>
</template>

<script>
    import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'
    import {mapState} from "vuex"
    import enquireScreen from '@/utils/device'

    export default {
        data() {
            return {
                local: zhCN
            }
        },
        computed: {
            ...mapState(['network'])
        },
        created() {
            let that = this
            enquireScreen(deviceType => {
                // tablet
                if (deviceType === 0) {
                    that.$store.commit('TOGGLE_DEVICE', 'mobile')
                    that.$store.dispatch('setSidebar', false)
                }
                // mobile
                else if (deviceType === 1) {
                    that.$store.commit('TOGGLE_DEVICE', 'mobile')
                    that.$store.dispatch('setSidebar', false)
                }
                else {
                    that.$store.commit('TOGGLE_DEVICE', 'desktop')
                    that.$store.dispatch('setSidebar', true)
                }

            })
        },
        methods: {
            // 通过跳转一个空页面再返回的方式来实现刷新当前页面数据的目的
            onRefresh() {
                this.$router.replace("/refresh")
            }
        }
    }
</script>

<style>
</style>
