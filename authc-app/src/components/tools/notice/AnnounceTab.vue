<template>
    <a-list>
        <a-list-item :key="index" v-for="(record,index) in announceList">
            <div style="margin-left: 5%;width: 80%">
                <p><a @click="showAnnounce(record)">{{ record.titile }}</a></p>
                <p style="color: rgba(0,0,0,.45);margin-bottom: 0px">{{ record.createTime }} 发布</p>
            </div>
            <div style="text-align: right">
                <a-tag @click="showAnnounce(record)" v-if="record.priority === 'L'" color="blue">一般消息</a-tag>
                <a-tag @click="showAnnounce(record)" v-if="record.priority === 'M'" color="orange">重要消息</a-tag>
                <a-tag @click="showAnnounce(record)" v-if="record.priority === 'H'" color="red">紧急消息</a-tag>
            </div>
        </a-list-item>
    </a-list>
</template>

<script>
    export default {
        name: 'AnnounceTab',
        props: {
            announceList: {
                type: Array,
                required: true
            }
        },
        methods: {
            showAnnounce(record) {
                this.$emit('showAnnounce', record)
            }
        }
    }
</script>
