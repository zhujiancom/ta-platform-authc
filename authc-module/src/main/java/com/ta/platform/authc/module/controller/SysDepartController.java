package com.ta.platform.authc.module.controller;

import com.alibaba.fastjson.JSON;
import com.ey.tax.toolset.core.StrUtil;
import com.ta.platform.authc.module.entity.SysDepart;
import com.ta.platform.authc.module.service.ISysDepartService;
import com.ta.platform.authc.module.vo.SysDepartTreeModel;
import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.api.vo.Result;
import com.ta.platform.common.constant.CacheConstant;
import com.ta.platform.common.constant.CommonConstant;
import com.ta.platform.common.service.ISysBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Creator: zhuji
 * Date: 4/28/2020
 * Time: 3:15 PM
 * Description:
 */
@Slf4j
@Api(tags="部门管理接口")
@RestController
@RequestMapping(value = "/sys/depart")
public class SysDepartController {
    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private ISysBaseService sysBaseService;


    @PostMapping(value = "/add")
    @CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
    public Result<?> add(@RequestBody SysDepart sysDepart){
        try {
            sysDepart.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
            boolean ok = sysDepartService.save(sysDepart);
            sysBaseService.addLog(StrUtil.format("新增部门【{}】", sysDepart.getDepartName()),CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
            if(ok){
                return Result.ok();
            }else{
                return Result.error();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Result.error();
    }

    @PutMapping(value = "/edit")
    @CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
    public Result<?> edit(@RequestBody SysDepart sysDepart){
        try {
            SysDepart departDb = sysDepartService.getById(sysDepart.getId());
            if(departDb == null){
                return Result.error("未找到对应的部门实体");
            }else{
                boolean ok = sysDepartService.updateById(sysDepart);
                if(ok){
                    return Result.ok();
                }else{
                    return Result.error();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return Result.error();
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @ApiOperation(value = "加载部门数据树形结构")
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<Object> queryTreeList() {
        try {
            List<SysDepartTreeModel> list = sysDepartService.queryTreeList();
            return Result.ok(JSON.toJSONString(list),"加载部门数据树形结构成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Result.error(ApiCode.FAIL.getCode(), e.getMessage());
        }
    }
}
