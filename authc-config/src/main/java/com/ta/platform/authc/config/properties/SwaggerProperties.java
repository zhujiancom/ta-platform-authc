package com.ta.platform.authc.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 3:11 PM
 * Description: Swagger配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "taplatform.swagger")
public class SwaggerProperties {
    /**
     * 是否启用Swagger
     */
    private boolean enable;

    /**
     * 扫描的基本包
     */
    private String basePackage;

    /**
     * 描述
     */
    private String description;

    /**
     * 标题
     */
    private String title;

    /**
     * 网址
     */
    private String url;

    /**
     * 版本
     */
    private String version;

}
