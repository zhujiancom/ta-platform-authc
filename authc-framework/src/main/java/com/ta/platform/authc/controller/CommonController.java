package com.ta.platform.authc.controller;

import com.ey.tax.toolset.core.io.FileUtil;
import com.ey.tax.toolset.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 3:37 PM
 * Description:
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys/common")
public class CommonController {
    @Value(value = "${taplatform.upload.path.root}")
    private String uploadPath;

    @RequestMapping(value = "/static/**", method = RequestMethod.GET)
    public void view(HttpServletRequest request, HttpServletResponse response) {
        String imgPath = extractPathFromPattern(request);
        OutputStream outputStream = null;
        try {
            imgPath = imgPath.replace("..", "");
            if (imgPath.endsWith(",")) {
                imgPath = imgPath.substring(0, imgPath.length() - 1);
            }
            String filePath = uploadPath + File.separator + imgPath;
            File file = new File(filePath);
            if (!FileUtil.exist(file)) {
                return;
            }
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "iso-8859-1"));
            outputStream = response.getOutputStream();
            FileUtil.writeToStream(file, outputStream);
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览文件失败" + e.getMessage());
        } finally {
            IoUtil.close(outputStream);
        }
    }

    /**
     * 把指定URL后的字符串全部截断当成参数
     * 这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
     *
     * @param request
     * @return
     */
    private static String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
}
