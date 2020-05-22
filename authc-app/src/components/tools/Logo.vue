<template>
    <div class="logo" :style="logoStyle">
        <router-link :to="{name: 'dashboard'}">
            <img :src="imgSrc" alt="logo">
        </router-link>

        <h1 v-if="showTitle">{{title}}</h1>
    </div>
</template>

<script>
    import {mixin} from "../../utils/mixin";

    export default {
        name: 'Logo',
        mixins: [mixin],
        data() {
            return {
                imgSrc: '',
                paddingLeftWidth: '24px'
            }
        },
        props: {
            title: {
                type: String,
                default: 'Tax Auto System',
                required: false
            },
            showTitle: {
                type: Boolean,
                default: false,
                required: false
            },
            collapsed: {
                type: Boolean,
                required: false,
                default: false
            }
        },
        created() {
            console.debug("create logo collapsed = ",this.collapsed)
            this.refreshLogo(this.collapsed)
        },
        computed:{
            logoStyle(){
                let style = {}
                if(this.collapsed){
                    style['padding-left'] = '0px'
                }else{
                    style={}
                }
                return style
            }
        },
        watch:{
            collapsed (val) {
                console.debug('%c collapsed='+this.collapsed+', val = '+val,'color: blue;')
                this.refreshLogo(val)
            },
        },
        methods: {
            refreshLogo(val){
                if(this.navTheme === 'dark'){
                    if(val){
                        this.imgSrc = require('@/assets/logo-mini-white.png')
                        this.paddingLeftWidth='0px'
                    }else{
                        this.imgSrc = require('@/assets/logo-white.png')
                    }
                }else{
                    if(val) {
                        this.imgSrc = require('@/assets/logo-mini-black.png')
                        this.paddingLeftWidth='0px'
                    }else{
                        this.imgSrc = require('@/assets/logo-black.png')
                    }
                }
            }
        }
    }
</script>

<style lang="less" scoped>
    /* 首页布局顶部的高度 */
    @height: 59px;

    .sider {
        box-shadow: none !important;

        .logo {
            height: @height !important;
            line-height: @height !important;
            box-shadow: none !important;
            transition: background 300ms;

            a {
                color: white;

                &:hover {
                    color: rgba(255, 255, 255, 0.8);
                }
            }
        }

        &.light .logo {
            background-color: @primary-color;
        }
    }
</style>
