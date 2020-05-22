<template>
    <a-input :placeholder="placeholder" :value="inputVal" @input="backValue"></a-input>
</template>

<script>
    const QUERY_LIKE = 'like';
    const QUERY_NE = 'ne';
    const QUERY_GE = 'ge'; //大于等于
    const QUERY_LE = 'le'; //小于等于

    export default {
        name: 'ZQueryInput',
        props:{
            value:{
                type:String,
                required:false
            },
            type:{
                type:String,
                required:false,
                default:QUERY_LIKE
            },
            placeholder:{
                type:String,
                required:false,
                default:''
            }
        },
        watch:{
            value:{
                immediate:true,
                handler:function(){
                    this.initVal();
                }
            }
        },
        model: {
            prop: 'value',
            event: 'change'
        },
        data(){
            return {
                inputVal:''
            }
        },
        methods:{
            initVal(){
                if(!this.value){
                    this.inputVal = ''
                }else{
                    let text = this.value
                    switch (this.type) {
                        case QUERY_LIKE:
                            text = text.substring(1,text.length-1);
                            break;
                        case QUERY_NE:
                            text = text.substring(1);
                            break;
                        case QUERY_GE:
                            text = text.substring(2);
                            break;
                        case QUERY_LE:
                            text = text.substring(2);
                            break;
                        default:
                    }
                    this.inputVal = text
                }
            },
            backValue(e){
                let text = e.target.value
                switch (this.type) {
                    case QUERY_LIKE:
                        text = "*"+text+"*";
                        break;
                    case QUERY_NE:
                        text = "!"+text;
                        break;
                    case QUERY_GE:
                        text = ">="+text;
                        break;
                    case QUERY_LE:
                        text = "<="+text;
                        break;
                    default:
                }
                this.$emit("change",text)
            }
        }
    }
</script>
