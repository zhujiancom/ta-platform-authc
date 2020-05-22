<template>
    <a-popover
            trigger="click"
            placement="bottom"
            :autoAjustOverflow="true"
            :arrowPointAtCenter="true"
            overlayClassName="header-notice-wrapper"
            :overlayStyle="{width: '300px', top: '50px'}"
            @visibleChange="handleHoverChange"
            >
        <template slot="content">
            <a-spin :spinning="loading">
                <a-tabs>
                    <a-tab-pane :tab="noticeTitle" key="1">
                        <notice-tab :notice-list="noticeList" @showNotice="showNotice"></notice-tab>
                    </a-tab-pane>
                    <a-tab-pane :tab="announceTitle" key="2">
                        <announce-tab :announce-list="announceList" @showAnnounce="showNotice"></announce-tab>
                    </a-tab-pane>
                </a-tabs>
                <div style="margin-top: 5px;text-align: center">
                    <a-button @click="myNotice()" type="dashed" block>查看更多</a-button>
                </div>
            </a-spin>
        </template>
        <span @click="fetchNotice" class="header-notice">
          <a-badge :count="msgTotalCount" showZero>
            <a-icon style="font-size: 16px; padding: 4px" type="bell" />
          </a-badge>
        </span>
    </a-popover>
</template>

<script>
    import NoticeTab from "./NoticeTab";
    import AnnounceTab from "./AnnounceTab";
    import commonAPI from "../../../api/services/common-api";
    import {mapGetters} from "vuex";

    export default {
        name: 'HeaderNotice',
        components: {
            AnnounceTab
            ,NoticeTab
        },
        data() {
            return {
                visible: false,
                noticeCount: 0,
                announceCount: 0,
                loading: false,
                noticeList: [],
                announceList: []
            }
        },
        computed: {
            announceTitle() {
                return "系统消息(" + this.announceCount + ")"
            },
            noticeTitle(){
                return "通知("+this.noticeCount+")"
            },
            msgTotalCount(){
                return parseInt(this.noticeCount)+parseInt(this.announceCount)
            }
        },
        mounted() {
            this.loadData()
        },
        methods:{
            ...mapGetters(["userInfo","nickname"]),
            hide() {
                this.visible = false;
            },
            fetchNotice(){
                if (this.loadding) {
                    this.loadding = false
                    return
                }
                this.loadding = true
                setTimeout(() => {
                    this.loadding = false
                }, 20000)
            },
            showNotice(record){
                console.log("record = ", record)
            },
            myNotice(){

            },
            handleHoverChange(){

            },
            loadData(){
                try {
                    console.dir(this.userInfo().id)
                    // 获取系统消息
                    commonAPI.fetchNotice({userId:this.userInfo().id}).then((res) => {
                        if (res.success) {
                            this.noticeList = res.data.noticeList;
                            this.noticeCount = res.data.noticeCount;
                            this.announceList = res.data.announceList;
                            this.announceCount = res.data.announceCount;
                        }
                    }).catch(error => {
                        console.log("系统消息通知异常",error);//这行打印permissionName is undefined
                    });
                } catch (err) {
                    console.log("通知异常",err);
                }
            }
        }
    }
</script>
