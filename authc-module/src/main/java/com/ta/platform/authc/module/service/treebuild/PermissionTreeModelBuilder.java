package com.ta.platform.authc.module.service.treebuild;

import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.core.converter.ConvertUtil;
import com.ey.tax.toolset.core.map.MapUtil;
import com.ta.platform.authc.module.entity.SysPermission;
import com.ta.platform.authc.module.vo.SysPermissionTreeModel;
import com.ta.platform.common.service.ITreeModelBuildCallback;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
    public SysPermissionTreeModel build(SysPermission src, Map<String,Object> parameter) {
        SysPermissionTreeModel.SysPermissionTreeModelBuilder builder = SysPermissionTreeModel.builder();
        builder.key(src.getId()).parentId(src.getParentId())
                .slotTitle(src.getName())
                .label(src.getName())
                .value(src.getId())
                .ruleFlag(src.getRuleFlag());
//        if(parameter.get("title") != null){
//            builder.title((String) parameter.get("title"));
//            builder.scopedSlots(MapUtil.of("title",(String) parameter.get("title")));
//        }
//        if(!ConvertUtil.toBool(parameter.get("a-tree"),false)){
//            builder.icon(src.getIcon());
//        }
        Map<String, Object> slotMap = new HashMap<>();
        slotMap.put("title", "hasDataRule");
        slotMap.put("icon", src.getIcon());
        builder.scopedSlots(slotMap);
        SysPermissionTreeModel model = builder.build();
        return model;
    }
}
