<template>
        <a-list>
            <a-list-item :key="index" v-for="(record, index) in noticeList">
                <div style="margin-left: 5%; width: 80%">
                    <p><a @click="showNotice(record)">{{record.title}}</a></p>
                    <p style="color: rgba(0,0,0,.45);margin-bottom: 0px">{{ record.createTime }} 发布</p>
                </div>
                <div style="text-align: right">
                    <a-tag @click="showNotice(record)" v-if="record.priority === 'L'" color="blue">一般消息</a-tag>
                    <a-tag @click="showNotice(record)" v-if="record.priority === 'M'" color="orange">重要消息</a-tag>
                    <a-tag @click="showNotice(record)" v-if="record.priority === 'H'" color="red">紧急消息</a-tag>
                </div>
            </a-list-item>
        </a-list>
</template>

<script>
    export default {
        name: 'NoticeTab',
        props:{
            noticeList:{
                type: Array,
                required: true
            }
        },
        methods:{
            showNotice(record){
                this.$emit('showNotice', record)
            }
        }
    }
</script>
