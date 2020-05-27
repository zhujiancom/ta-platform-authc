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
        <show-announcement ref="showAnnouncement"></show-announcement>
    </a-popover>
</template>

<script>
    import NoticeTab from "./NoticeTab";
    import AnnounceTab from "./AnnounceTab";
    import commonAPI from "../../../api/services/common-api";
    import {mapGetters} from "vuex";
    import ShowAnnouncement from "./ShowAnnouncement";

    export default {
        name: 'HeaderNotice',
        components: {
            ShowAnnouncement,
            AnnounceTab
            ,NoticeTab
        },
        data() {
            return {
                hovered: false,
                noticeCount: 0,
                announceCount: 0,
                loading: false,
                noticeList: [],
                announceList: [],
                webSock: null,
                heartCheck:null,
                lockReconnect:false,
            }
        },
        computed: {
            announceTitle() {
                return "通知("+this.announceCount+")"
            },
            noticeTitle(){
                return "系统消息(" + this.noticeCount + ")"
            },
            msgTotalCount(){
                return parseInt(this.noticeCount)+parseInt(this.announceCount)
            }
        },
        mounted() {
            this.loadData()
            this.initWebSocket()
            this.heartCheckFun()
        },
        methods:{
            ...mapGetters(["userInfo","nickname"]),
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
                commonAPI.readNoticeByUser({id: record.id, userId: this.userInfo().id}).then((res) => {
                    if(res.success){
                        this.loadData(1)
                    }
                })
                this.hovered = false
                this.$refs.showAnnouncement.detail(record)
            },
            myNotice(){

            },
            handleHoverChange(visible){
                this.hovered = visible;
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
            },
            initWebSocket(){
                console.log("----- 初始化webSocket连接")
                let userId = this.userInfo().id
                // let url = window._CONFIG['domainURL'].replace("https://","wss://").replace("http://","ws://")+"/websocket/"+userId
                let url = "ws://127.0.0.1:9092/core/websocket/"+userId
                console.log("websocket url = ",url)
                this.webSock = new WebSocket(url)
                this.webSock.onopen = this.onOpen
                this.webSock.onerror = this.onError
                this.webSock.onmessage = this.onMessage
                this.webSock.onclose = this.onClose
            },
            onOpen(){
                console.log("------WebSocket连接成功")
                // 心跳检测重置
                this.heartCheck.reset().start()
            },
            onError(){
                console.log("-----WebSocket连接发生错误")
                this.reconnect()
            },
            onMessage(e){
                console.log("-----WebSocket接受消息-------", e.data)
                let data = eval("("+e.data+")") //解析对象
                if(data.cmd == "topic"){
                    // 系统通知
                    this.loadData()
                }else if(data.cmd == "user"){
                    // 用户消息
                    this.loadData()
                }
                //心跳检测重置
                this.heartCheck.reset().start();
            },
            onClose(e){
                console.log("------WebSocket关闭连接------", e.code)
                this.reconnect()
            },
            onSend(text){
                console.log("------WebSocket发送数据------", text)
                try {
                    this.webSock.send(text)
                } catch (err) {
                    console.log("------WebSocket发送数据失败----",err)
                }
            },
            reconnect(){
                let that = this
                if(that.lockReconnect){
                    return
                }
                that.lockReconnect = true
                //没连接上会一直重连，设置延迟避免请求过多
                setTimeout(function () {
                    console.info("尝试重连...");
                    that.initWebSocket();
                    that.lockReconnect = false;
                }, 5000);
            },
            heartCheckFun(){
                console.log("------WebSocket心跳检测每20s检测一次-----")
                let that = this
                that.heartCheck = {
                    timeout: 60000,
                    timeoutObj: null,
                    serverTimeoutObj: null,
                    reset: function(){
                        clearTimeout(this.timeoutObj)
                        return this
                    },
                    start: function () {
                        let self = this
                        self.timeoutObj = setTimeout(function () {
                            // 客户端发送心跳， 后端收到后返回心跳消息
                            that.onSend("HeartBeat")
                            console.log("------ 客户端发送心跳消息：HeartBeat")
                        }, this.timeout)
                    }
                }
            }
        }
    }
</script>
