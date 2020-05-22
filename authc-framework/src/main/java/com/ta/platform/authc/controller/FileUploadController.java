package com.ta.platform.authc.controller;

import com.ey.tax.toolset.core.StrUtil;
import com.ta.platform.authc.service.FileUploader;
import com.ta.platform.common.api.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Creator: zhuji
 * Date: 4/22/2020
 * Time: 10:55 AM
 * Description:
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys/file")
public class FileUploadController {

    /**
     * 本地：local minio：minio 阿里：alioss
     */
    @Value(value="${taplatform.upload.type}")
    private String uploadType;

    @Autowired
    private List<FileUploader> fileUploaderList;

    @PostMapping(value = "/upload")
    public Result upload(HttpServletRequest request, HttpServletResponse response){
        Result<?> result = new Result<>();

        String bizPath = StrUtil.nullToDefault(request.getParameter("biz"),"");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("bizPath", bizPath);

        String storePath = "";
        try {
            Optional<FileUploader> fileUploaderOptional = fileUploaderList.stream().filter(f -> f.support(uploadType)).findAny();
            if(fileUploaderOptional.isPresent()){
                storePath = fileUploaderOptional.get().upload(file, parameter);
            }
            result.setMessage(storePath);
            result.setSuccess(true);
        } catch (FileUploadException e) {
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

}
