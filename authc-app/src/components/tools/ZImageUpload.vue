<template>
    <a-upload
        name="file"
        list-type="picture-card"
        :multiple="isMultiple"
        :action="uploadAction"
        :headers="headers"
        :data="{biz: bizPath}"
        :fileList="fileList"
        :before-upload="beforeUpload"
        :disabled="disabled"
        :show-upload-list="isMultiple"
        @change="handleChange"
        @preview="handlePreview">
        <img v-if="!isMultiple && picUrl" :src="getAvatarView()" >
        <div v-else>
            <a-icon :type="uploadLoading ? 'loading' : 'plus'"/>
            <div>{{text}}</div>
        </div>
        <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel()">
            <img alt="example" :src="previewImage">
        </a-modal>
    </a-upload>
</template>

<script>
    import Vue from 'vue'
    import {getFileAccessHttpUrl} from "../../utils/util";
    import {ACCESS_TOKEN} from "../../store/mutation-types";

    const uidGenerator=()=>{
        return '-'+parseInt(Math.random()*10000+1,10);
    }
    const getFileName=(path)=>{
        if(path.lastIndexOf("\\")>=0){
            let reg=new RegExp("\\\\","g");
            path = path.replace(reg,"/");
        }
        return path.substring(path.lastIndexOf("/")+1);
    }

    export default {
        name:'ZImageUpload',
        data() {
            return {
                uploadAction: window._CONFIG['domainURL']+'/sys/file/upload',
                urlView:window._CONFIG['staticDomainURL'],
                uploadLoading: false,
                picUrl: false,
                headers:{},
                fileList: [],
                previewImage: "",
                previewVisible: false
            }
        },
        props:{
            text:{
                type:String,
                required:false,
                default:"上传"
            },
            disabled:{
                type:Boolean,
                required:false,
                default: false
            },
            // 业务路径， biz = business 缩写， 使用阿里云存储， 这个属性一定要有值
            bizPath:{
                type:String,
                required:false,
                default:"tmp"
            },
            value:{
                type:[String,Array],
                required:false
            },
            isMultiple:{
                type:Boolean,
                required:false,
                default: false
            }
        },
        watch:{
            value(val){
                if(val instanceof Array){
                    this.initFileList(val.join(','))
                }else{
                    this.initFileList(val)
                }
            }
        },
        created() {
          const token = Vue.ls.get(ACCESS_TOKEN)
          this.headers = {"X-Access-Token":token}
        },
        methods:{
            initFileList(paths){
                if(!paths || paths.length ==0){
                    this.fileList = [];
                    return
                }
                this.picUrl = true
                let fileList = []
                let arr = paths.split(',')
                for(let i = 0;i<arr.length;i++){
                    let url = getFileAccessHttpUrl(arr[i], this.urlView, "http")
                    fileList.push({
                        uid: uidGenerator(),
                        name: getFileName(arr[i]),
                        status: 'done',
                        url: url,
                        response:{
                            status: 'history',
                            message: arr[i]
                        }
                    })
                }
                this.fileList = fileList
            },
            beforeUpload(file){
                let fileType = file.type
                if(fileType.indexOf('image') < 0){
                    this.$message.warning("请上传图片")
                    return false
                }
            },
            handleChange(info){
                this.picUrl = false
                let fileList = info.fileList
                if(info.file.status === 'done'){
                    if(info.file.response.success){
                        this.picUrl = true
                        fileList = fileList.map((file) => {
                            if(file.response){
                                file.url = file.response.message
                            }
                            return file
                        })
                    }
                    this.handlePathChange()
                    this.$message.success(`${info.file.name} 上传成功!`);
                }else if(info.file.status === 'error') {
                    this.$message.error(`${info.file.name} 上传失败.`);
                }else if(info.file.status === 'removed'){
                    this.handleDelete(info.file)
                    this.handlePathChange()
                }
                this.fileList = fileList
            },
            getAvatarView(){
                if(this.fileList.length>0){
                    let url = this.fileList[0].url
                    console.log("url = ", url)
                    return getFileAccessHttpUrl(url,this.urlView,"http")
                }
            },
            handlePathChange(){
                let uploadFiles = this.fileList
                let path = ''
                if(!uploadFiles || uploadFiles.length==0){
                    path = ''
                }
                let arr = [];
                if(!this.isMultiple){
                    arr.push(uploadFiles[uploadFiles.length-1].response.message)
                }else{
                    for(let i=0;i<uploadFiles.length;i++){
                        arr.push(uploadFiles[i].response.message)
                    }
                }
                if(arr.length>0){
                    path = arr.join(",")
                }
                this.$emit('change', path);
            },
            handleDelete(file){
                //如有需要新增 删除逻辑
                console.log(file)
            },
            handleCancel() {
                this.close();
                this.previewVisible = false;
            },
            // 预览
            handlePreview (file) {
                this.previewImage = file.url || file.thumbUrl
                this.previewVisible = true
            }
        },
        model: {
            prop: 'value',
            event: 'change'
        }
    }
</script>
