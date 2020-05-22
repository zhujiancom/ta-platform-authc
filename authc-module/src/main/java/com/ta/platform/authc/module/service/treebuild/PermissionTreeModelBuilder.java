package com.ta.platform.authc.module.service.treebuild;

import com.ey.tax.toolset.core.StrUtil;
import com.ta.platform.authc.module.entity.SysPermission;
import com.ta.platform.authc.module.vo.SysPermissionTreeModel;
import com.ta.platform.common.service.ITreeModelBuildCallback;
import org.springframework.stereotype.Component;

/**
 * Creator: zhuji
 * Date: 5/7/2020
 * Time: 2:51 PM
 * Description:
 */
@Component
public class PermissionTreeModelBuilder implements ITreeModelBuildCallback<SysPermission, SysPermissionTreeModel> {
    @Override
    public boolean filter(SysPermission src, String parentId) {
        if(StrUtil.isEmpty(parentId)){
            return StrUtil.isEmpty(src.getParentId());
        }else{
            return parentId.equals(src.getParentId());
        }
    }

    @Override
    public SysPermissionTreeModel build(SysPermission src) {
        SysPermissionTreeModel model = SysPermissionTreeModel
                .builder()
                .key(src.getId())
                .title(src.getName())
                .icon(src.getIcon())
                .parentId(src.getParentId())
                .slotTitle(src.getName())
                .label(src.getName())
                .value(src.getId())
                .build();
        return model;
    }
}
