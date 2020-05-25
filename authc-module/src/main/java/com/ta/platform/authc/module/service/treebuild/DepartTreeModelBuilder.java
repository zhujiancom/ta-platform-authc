package com.ta.platform.authc.module.service.treebuild;

import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.core.converter.ConvertUtil;
import com.ta.platform.authc.module.entity.SysDepart;
import com.ta.platform.authc.module.vo.SysDepartTreeModel;
import com.ta.platform.common.service.ITreeModelBuildCallback;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Creator: zhuji
 * Date: 5/7/2020
 * Time: 4:00 PM
 * Description: 部门树形结构数据
 */
@Component
public class DepartTreeModelBuilder implements ITreeModelBuildCallback<SysDepart, SysDepartTreeModel> {
    @Override
    public boolean filter(SysDepart src, String parentId) {
        if (StrUtil.isEmpty(parentId)) {
            return StrUtil.isEmpty(src.getParentId());
        } else {
            return parentId.equals(src.getParentId());
        }
    }

    @Override
    public SysDepartTreeModel build(SysDepart src, Map<String, Object> parameter) {
        SysDepartTreeModel tree = SysDepartTreeModel.builder()
                .id(src.getId())
                .key(src.getId())
                .value(src.getId())
                .title(src.getDepartName())
                .disableCheckbox(!ConvertUtil.toBool(src.getStatus(), false))
                .build();
        return tree;
    }
}
