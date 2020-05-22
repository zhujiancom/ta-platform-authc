<template>
    <!-- 侧边栏模式 -->
    <a-drawer
            v-if="device === 'mobile'"
            :wrapClassName="'drawer-sider ' + theme"
            placement="left"
            @close="() => this.$emit('toggle')"
            :closable="false"
            :visible="collapsed"
            width="200px"
    >
        <a-layout-sider
                :class="['sider', isDesktop() ? null : 'shadow', theme, fixSiderbar ? 'ant-fixed-sidemenu' : null ]"
                width="200px"
                :collapsible="collapsible"
                :trigger="null">
            <logo :collapsed="false"/>
            <s-menu :collapsed="false"
                    :menu="menus"
                    :theme="theme"
                    @select="menuSelect"
                    :mode="mode"
                    :style="menuStyle">
            </s-menu>
        </a-layout-sider>
    </a-drawer>
    <a-layout-sider v-else
                    :class="['sider', isDesktop() ? null : 'shadow', theme, fixSiderbar ? 'ant-fixed-sidemenu' : null ]"
                    width="200px"
                    :collapsible="collapsible"
                    v-model="collapsed"
                    :trigger="null">
        <logo :collapsed="collapsed"/>
        <s-menu :collapsed="collapsed"
                :menu="menus"
                :theme="theme"
                @select="myMenuSelect"
                :mode="mode"
                :style="menuStyle">
        </s-menu>
    </a-layout-sider>
</template>

<script>
    import Logo from "../tools/Logo";
    import {mixin, mixinDevice} from "../../utils/mixin";
    import SMenu from './index'

    export default {
        name: "SideMenu",
        components: {
            Logo,
            SMenu
        },
        mixins: [mixin, mixinDevice],
        props: {
            mode: {
                type: String,
                required: false,
                default: 'inline'
            },
            theme: {
                type: String,
                required: false,
                default: 'dark'
            },
            collapsible: {
                type: Boolean,
                required: false,
                default: false
            },
            collapsed: {
                type: Boolean,
                required: false,
                default: false
            },
            menus: {
                type: Array,
                required: true
            }
        },
        computed: {
            menuStyle() {
                let style = {'padding': '0'}
                if (this.fixSiderbar) {
                    style['height'] = 'calc(100% -59px)'
                    style['overflow'] = 'auto'
                    style['overflow-x'] = 'hidden'
                }
                return style
            }
        },
        methods: {
            menuSelect() {
                if (!this.isDesktop()) {
                    this.collapsed = false
                }
            },
            //动态路由title显示配置的菜单title而不是其对应路由的title
            myMenuSelect(value) {
                //此处触发动态路由被点击事件
                this.findMenuBykey(this.menus, value.key)
                this.$emit("dynamicRouterShow", value.key, this.activeMenu.meta.title)
                //  修复刷新后菜单Tab名字显示异常
                let storeKey = 'route:title:' + this.activeMenu.path
                this.$ls.set(storeKey, this.activeMenu.meta.title)
                //  修复刷新后菜单Tab名字显示异常
            },
            findMenuBykey(menus, key) {
                for (let i of menus) {
                    if (i.path == key) {
                        this.activeMenu = {...i}
                    } else if (i.children && i.children.length > 0) {
                        this.findMenuBykey(i.children, key)
                    }
                }
            },
        }
    }
</script>
<style lang="less" scoped>
    .sider {
        @scrollBarSize: 10px;

        ul.ant-menu {
            /* 定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/

            &::-webkit-scrollbar {
                width: @scrollBarSize;
                height: @scrollBarSize;
                background-color: transparent;
                display: none;
            }

            & .-o-scrollbar {
                display: none;
            }

            /* 兼容IE */
            -ms-overflow-style: none;
            -ms-scroll-chaining: chained;
            -ms-content-zooming: zoom;
            -ms-scroll-rails: none;
            -ms-content-zoom-limit-min: 100%;
            -ms-content-zoom-limit-max: 500%;
            -ms-scroll-snap-type: proximity;
            -ms-scroll-snap-points-x: snapList(100%, 200%, 300%, 400%, 500%);

            /* 定义滚动条轨道 */

            &::-webkit-scrollbar-track {
                background-color: transparent;
            }

            /* 定义滑块 */

            &::-webkit-scrollbar-thumb {
                border-radius: @scrollBarSize;
                background-color: #eee;
                box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.1);

                &:hover {
                    background-color: #dddddd;
                }

                &:active {
                    background-color: #bbbbbb;
                }
            }
        }

        /** 暗色系滚动条样式 */

        &.dark ul.ant-menu {
            &::-webkit-scrollbar-thumb {
                background-color: #666666;

                &:hover {
                    background-color: #808080;
                }

                &:active {
                    background-color: #999999;
                }
            }
        }
    }
</style>
<style lang="less">
    .ant-menu.ant-menu-root {
        & > .ant-menu-item:first-child {
            background-color: transparent;

            & > a, & > a:hover {
                color: rgba(0, 0, 0, 0.65);
            }

            &.ant-menu-item-selected {
                & > a, & > a:hover {
                    color: @primary-color;
                }
            }
        }

        &.ant-menu-dark > .ant-menu-item:first-child {
            & > a, & > a:hover {
                color: rgba(255, 255, 255, 0.65);
            }

            &.ant-menu-item-selected {
                & > a, & > a:hover {
                    color: rgba(255, 255, 255, 1);
                }
            }
        }
    }
</style>
