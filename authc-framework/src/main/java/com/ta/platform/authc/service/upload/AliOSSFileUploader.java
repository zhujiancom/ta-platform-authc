package com.ta.platform.authc.service.upload;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectResult;
import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.core.io.FileUtil;
import com.ta.platform.authc.service.FileUploader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Creator: zhuji
 * Date: 4/22/2020
 * Time: 11:59 AM
 * Description: 阿里云存储上传
 */
@Slf4j
@Component
public class AliOSSFileUploader implements FileUploader {
    @Value("${taplatform.oss.endpoint}")
    private String endPoint;
    @Value("${taplatform.oss.accessKey}")
    private String accessKeyId;
    @Value("${taplatform.oss.secretKey}")
    private String accessKeySecret;
    @Value("${taplatform.oss.bucketName}")
    private String bucketName;
    @Value("${taplatform.oss.staticDomain}")
    private String staticDomain;

    /**
     * oss 工具客户端
     */
    private static OSSClient ossClient = null;
    private static String FILE_URL;

    @Override
    public String upload(MultipartFile mf, Map<String, Object> parameter) throws FileUploadException{
        initOSS();
        String fileDir = (String) parameter.get("bizPath");
        if(StrUtil.isBlank(fileDir)){
            throw new FileUploadException("使用阿里云文件上传时，必须添加目录！");
        }
        try {
            String orgName = mf.getName();
            String fileName = FileUtil.mainName(orgName) + "_" + System.currentTimeMillis() + StrUtil.DOT + FileUtil.extName(orgName);

            String fileUrl = FileUtil.normalize(fileDir+ File.separator+fileName);

            if (StrUtil.isNotEmpty(staticDomain) && staticDomain.toLowerCase().startsWith("http")) {
                FILE_URL = staticDomain + "/" + fileUrl;
            } else {
                FILE_URL = "https://" + bucketName + "." + endPoint + "/" + fileUrl;
            }
            PutObjectResult result = ossClient.putObject(bucketName, fileUrl, mf.getInputStream());
            // 设置权限(公开读)
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                log.info("------OSS文件上传成功------" + fileUrl);
            }
        } catch (IOException e) {
            log.error("----OSS文件上传失败----",e);
            throw new FileUploadException("OSS文件上传失败",e);
        }
        return FILE_URL;
    }

    @Override
    public boolean support(String uploadType) {
        return UploadType.ALIOSS.getCode().equals(uploadType);
    }

    /**
     * 初始化客户端
     * @return
     */
    private OSSClient initOSS(){
        if(ossClient == null){
            ossClient = new OSSClient(endPoint,
                    new DefaultCredentialProvider(accessKeyId, accessKeySecret),
                    new ClientConfiguration());
        }
        return ossClient;
    }
}
