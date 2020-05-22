<template>
    <a-layout-header
            v-if="!headerBarFixed"
            :class="[fixedHeader && 'ant-header-fixedHeader', sidebarOpened ? 'ant-header-side-opened' : 'ant-header-side-closed']"
            :style="{ padding: '0'}">
        <div v-if="mode === 'sidemenu'" class="header" :class="theme">
            <a-icon
                    v-if="device==='mobile'"
                    class="trigger"
                    :type="collapsed ? 'menu-fold' : 'menu-unfold'"
                    @click="toggle"></a-icon>
            <a-icon
                    v-else
                    class="trigger"
                    :type="collapsed ? 'menu-unfold' : 'menu-fold'"
                    @click="toggle"/>

            <span v-if="device === 'desktop'">欢迎使用税务自动化系统</span>
            <span v-else>Tax Auto System</span>

            <user-menu class="header-index-right" :theme="theme" :style="topMenuStyle.headerIndexRight"></user-menu>
        </div>
    </a-layout-header>
</template>

<script>
    import {mixin} from "../../utils/mixin";
    import UserMenu from "../menu/UserMenu";

    export default {
        name: 'GlobalHeader',
        components: {UserMenu},
        mixins: [mixin],
        props: {
            mode: {
                type: String,
                default: 'sidemenu'
            },
            menus: {
                type: Array,
                required: true
            },
            theme: {
                type: String,
                required: false,
                default: 'dark'
            },
            collapsed: {
                type: Boolean,
                required: false,
                default: false
            },
            device: {
                type: String,
                required: false,
                default: 'desktop'
            }
        },
        data() {
            return {
                headerBarFixed: false,
                // 顶部导航栏过长时显示更多按钮
                topMenuStyle: {
                    headerIndexLeft: {},
                    topNavHeader: {},
                    headerIndexRight: {},
                    topMenuStyle: {}
                }
            }
        },
        watch: {
            /** 监听设备变化 */
            device() {
                if (this.mode === 'topmenu') {
                    this.buildTopMenuStyle()
                }
            },
            /** 监听导航栏模式变化 */
            mode(newVal) {
                if (newVal === 'topmenu') {
                    this.buildTopMenuStyle()
                }
            }
        },
        mounted() {
            window.addEventListener('scroll', this.handleScroll)
            // 顶部导航栏过长时显示更多按钮-----
            if (this.mode === 'topmenu') {
                this.buildTopMenuStyle()
            }
        },
        methods: {
            handleScroll() {
                if (this.autoHideHeader) {
                    let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
                    if (scrollTop > 100) {
                        this.headerBarFixed = true
                    } else {
                        this.headerBarFixed = false
                    }
                } else {
                    this.headerBarFixed = false
                }
            },
            toggle() {
                this.$emit('toggle')
            },
            buildTopMenuStyle() {
                if (this.mode === 'topmenu') {
                    if (this.device == 'mobile') {
                        // 手机端需要清空样式， 否则显示错乱
                        this.topMenuStyle.topNavHeader = {}
                        this.topMenuStyle.topMenuStyle = {}
                        this.topMenuStyle.headerIndexLeft = {}
                        this.topMenuStyle.headerIndexRight = {}
                    } else {
                        let rightWidth = '360px'
                        this.topMenuStyle.topNavHeader = {'min-width': '165px'}
                        this.topMenuStyle.topMenuStyle = {'width': 'calc(100% - 165px)'}
                        this.topMenuStyle.headerIndexRight = {'min-width': rightWidth}
                        this.topMenuStyle.headerIndexLeft = {'width': `calc(100% - ${rightWidth})`}
                    }
                }
            },
        }
    }
</script>
<style lang="less" scoped>
    @height: 59px;

    .layout {

        .top-nav-header-index {

            .header-index-wide {
                margin-left: 10px;

                .ant-menu.ant-menu-horizontal {
                    height: @height;
                    line-height: @height;
                }
            }

            .trigger {
                line-height: 64px;

                &:hover {
                    background: rgba(0, 0, 0, 0.05);
                }
            }
        }

        .header {
            z-index: 2;
            color: white;
            height: @height;
            background-color: @primary-color;
            transition: background 300ms;

            /* dark 样式 */

            &.dark {
                color: #000000;
                box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
                background-color: white !important;
            }
        }

        .header, .top-nav-header-index {
            &.dark .trigger:hover {
                background: rgba(0, 0, 0, 0.05);
            }
        }
    }

    .ant-layout-header {
        height: @height;
        line-height: @height;
    }
</style>
