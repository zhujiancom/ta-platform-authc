package com.ta.platform.authc.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 3:09 PM
 * Description: Shiro配置映射类
 */
@Data
@Component
@ConfigurationProperties(prefix = "taplatform.shiro")
public class ShiroProperties {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 路径权限配置
     */
    private String filterChainDefinitions;

    /**
     * 设置无需权限路径集合
     */
    private List<String[]> anon;
}
