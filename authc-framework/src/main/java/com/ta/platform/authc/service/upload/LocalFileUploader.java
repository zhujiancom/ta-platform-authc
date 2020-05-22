package com.ta.platform.authc.service.upload;

import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.core.io.FileUtil;
import com.ta.platform.authc.service.FileUploader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Creator: zhuji
 * Date: 4/22/2020
 * Time: 11:51 AM
 * Description: 本地文件上传
 */
@Slf4j
@Component
public class LocalFileUploader implements FileUploader {
    @Value(value = "${taplatform.upload.path.root}")
    private String uploadPath;

    @Override
    public String upload(MultipartFile mf, Map<String, Object> parameter) throws FileUploadException {
        try {
            String bizPath = (String) parameter.get("bizPath");
            String ctxPath = uploadPath;
            String fileName = null;
            // 创建文件根目录
            File rootDir = FileUtil.mkdir(ctxPath+File.separator+bizPath+File.separator);
            String orgName = mf.getOriginalFilename();// 获取文件名
            fileName = FileUtil.mainName(orgName) + "_" + System.currentTimeMillis() + StrUtil.DOT + FileUtil.extName(orgName);
            String savePath = rootDir.getPath() + File.separator + fileName;
            File saveFile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), saveFile);
            String dbPath = FileUtil.normalize(bizPath + File.separator + fileName);
            return dbPath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileUploadException("上传本地文件失败！",e);
        }
    }

    @Override
    public boolean support(String uploadType) {
        return UploadType.LOCAL.getCode().equals(uploadType);
    }
}
