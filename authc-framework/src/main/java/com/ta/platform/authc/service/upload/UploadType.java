package com.ta.platform.authc.service.upload;

import java.util.EnumSet;

/**
 * Creator: zhuji
 * Date: 4/22/2020
 * Time: 11:42 AM
 * Description:
 */
public enum UploadType {
    LOCAL("local"), ALIOSS("alioss"), MINIO("minio"), UNKNOW("");

    private String code;

    UploadType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    UploadType getUploadType(String code) {
        EnumSet<UploadType> es = EnumSet.allOf(UploadType.class);
        for (UploadType type : es) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return UNKNOW;
    }
}
