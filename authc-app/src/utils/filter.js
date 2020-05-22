import Vue from "vue";

Vue.filter("ellipsis", (value,vlength=25)=>{
    if(!value){
        return "";
    }
    console.log('vlength: '+ vlength);
    if (value.length > vlength) {
        return value.slice(0, vlength) + '...'
    }
    return value
})
