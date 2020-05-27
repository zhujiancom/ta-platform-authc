<template>
    <a-modal :visible="visible"
             :width="modelStyle.width"
             :bodyStyle ="bodyStyle"
             @cancel="handleCancel"
             destroyOnClose>
        <template slot="title">
            <a-button icon="fullscreen" class="custom-btn" @click="handleClickToggleFullScreen"/>
        </template>
        <template slot="footer">
            <a-button key="back" @click="handleCancel">关闭</a-button>
            <a-button v-if="record.openType==='url'&&record.readFlag!=='1'" type="primary" @click="toHandle">去处理</a-button>
        </template>
        <a-card class="daily-article" :loading="loading">
            <a-card-meta
                    :title="record.title"
                    :description="'发布人：'+record.publisher + ' 发布时间： ' + record.publishTime">
            </a-card-meta>
            <a-divider />
            <span v-html="record.content" class="article-content"></span>
        </a-card>
    </a-modal>
</template>
<script>
    export default {
        name:'ShowAnnouncement',
        data() {
            return {
                visible: false,
                loading: false,
                record: {},
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 },
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 },
                },
                modelStyle:{
                    width: '60%',
                    style: { top: '20px' },
                    fullScreen: false
                },
                bodyStyle:{
                    padding: "0",
                    height:(window.innerHeight*0.8)+"px",
                    "overflow-y":"auto",

                },
            }
        },
        methods:{
            detail(record){
                this.visible = true;
                this.record = record;
            },
            handleCancel () {
                this.visible = false;
            },
            /** 切换全屏显示 */
            handleClickToggleFullScreen() {
                let mode = !this.modelStyle.fullScreen
                if (mode) {
                    this.modelStyle.width = '100%'
                    this.modelStyle.style.top = '20px'
                } else {
                    this.modelStyle.width = '60%'
                    this.modelStyle.style.top = '50px'
                }
                this.modelStyle.fullScreen = mode
            },
            toHandle(){
                if(this.record.openType==='url'&&this.record.readFlag!== '1'){
                    this.visible = false;
                    //链接跳转
                    this.$router.push({path: this.record.openPage})
                }
            },
        }
    }
</script>
