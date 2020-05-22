<template>
    <a-select
            v-model="selectValue"
            :placeholder="placeholder"
            :disabled="disabled"
            :getPopupContainer="(target) => target.parentNode"
    >
        <a-select-option :value="undefined">请选择</a-select-option>
        <a-select-option v-for="item in itemList" :key="item.value" :value="item.value">{{item.text}}</a-select-option>
    </a-select>
</template>
<script>
    import {isNull} from "../../utils/validate";
    import commonAPI from "../../api/services/common-api";

    export default {
        name: 'ZDictView2',
        data() {
            return {
                itemList : [],
                selectValue: []
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
            category: {
                type: String,
                required: true,
            },
            value:{
                required: true
            }
        },
        created() {
            this.buildItemList()
        },
        watch:{
            value: {
                immediate: true,
                handler(val) {
                    console.log("watch value , val = ", val)
                    if (val) {
                        this.selectValue = val
                    } else {
                        this.selectValue = []
                    }
                }
            },
            selectValue: {
                deep: true,
                handler(val) {
                    console.log("watch select value , val = "+val+", this.value = "+this.value)
                    if(!isNull(val)){
                        this.$emit('select', val)
                        this.$emit('input', val)
                        this.$emit('change', val)
                    }
                }
            }
        },
        methods:{
            buildItemList() {
                console.log("dictCode = ", this.category)
                let that = this
                commonAPI.fetchDictItems({dictCode:this.category}).then((res) => {
                    if(res.success){
                        that.itemList = res.data
                    }else{
                        that.$message.warning(res.message)
                    }
                    console.log(that.itemList)
                })
            }
        }
    }
</script>
