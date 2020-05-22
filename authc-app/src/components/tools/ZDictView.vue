<template>
    <a-radio-group v-if="tagType=='radio'" @change="handleInput" :value="value" :disabled="disabled">
        <a-radio v-for="(item, key) in itemList" :key="key" :value="item.value">{{ item.text }}</a-radio>
    </a-radio-group>

    <a-select v-else-if="tagType == 'select'"
              :value="value"
            :placeholder="placeholder"
            :disabled="disabled"
            :getPopupContainer="(target) => target.parentNode"
              @change="handleInput"
    >
        <a-select-option v-if="showTips" :value="undefined">请选择</a-select-option>
        <a-select-option v-for="(item,key) in itemList" :key="key" :value="item.value">{{item.text}}</a-select-option>
    </a-select>
</template>
<script>
    import commonAPI from "../../api/services/common-api";

    export default {
        name: 'ZDictView',
        data() {
            return {
                itemList: [],
                tagType:""
            }
        },
        props: {
            placeholder: {
                type: String,
                required: false,
                default: ''
            },
            disabled: {
                type: Boolean,
                required: false,
                default: false
            },
            dictCode: {
                type: String,
                required: true,
            },
            value: [String, Number],
            showTips: {
                type: Boolean,
                required: false,
                default: false
            },
            type: String,
            triggerChange: Boolean
        },
        created() {
            if(!this.type || this.type==="list"){
                this.tagType = 'select'
            }else{
                this.tagType = this.type
            }
        },
        watch: {
            dictCode:{
                immediate:true,
                handler(){
                    this.buildItemList()
                }
            },
        },
        methods: {
            buildItemList() {
                // console.log("dictCode = ", this.dictCode)
                let that = this
                commonAPI.fetchDictItems({dictCode: this.dictCode}).then((res) => {
                    if (res.success) {
                        that.itemList = res.data
                    } else {
                        that.$message.warning(res.message)
                    }
                    // console.log(that.itemList)
                })
            },
            handleInput(e){
                console.log("e = ", e)
                let val
                if(this.tagType == 'radio'){
                    val = e.target.value
                }else{
                    val = e
                }
                console.log("triggerChange = ", this.triggerChange)
                console.log("typeof val =", typeof val, "val = ", val)
                if(this.triggerChange){
                    this.$emit('change', val)
                }else{
                    this.$emit('input',val)
                }
            }
        },
    }
</script>
