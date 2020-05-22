<template>
    <div id="app">
        <div v-if="!network">
            <h3>������������</h3>
            <div @click="onRefresh">ˢ��</div>
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
            // ͨ����תһ����ҳ���ٷ��صķ�ʽ��ʵ��ˢ�µ�ǰҳ�����ݵ�Ŀ��
            onRefresh() {
                this.$router.replace("/refresh")
            }
        }
    }
</script>

<style>
</style>
