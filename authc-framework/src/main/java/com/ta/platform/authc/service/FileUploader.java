package com.ta.platform.authc.service;

import org.apache.commons.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Creator: zhuji
 * Date: 5/21/2020
 * Time: 12:47 PM
 * Description: 文件上传接口
 */
public interface FileUploader {
    String upload(MultipartFile mf, Map<String, Object> parameter) throws FileUploadException;

    boolean support(String uploadType);
}
