package cn.worth.sys.controller;

import cn.worth.common.annotation.CurrentUser;
import cn.worth.common.constant.CommonConstant;
import cn.worth.common.domain.LoginUser;
import cn.worth.common.utils.StringUtils;
import cn.worth.mysql.controller.BaseController;
import cn.worth.sys.domain.Tenant;
import cn.worth.sys.service.ITenantService;
import cn.worth.common.domain.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
@RestController
@RequestMapping("/tenant")
public class TenantController extends BaseController<ITenantService, Tenant> {

    @Autowired
    private ITenantService tenantService;

    @PostMapping("page")
    public R page(Page<Tenant> orgPage, Tenant org, @CurrentUser LoginUser loginUser){

        QueryWrapper entityWrapper = getEntityWrapper(org);

        Page<Tenant> page = tenantService.page(orgPage, entityWrapper);
        return R.success(page);
    }

    private QueryWrapper getEntityWrapper(Tenant tenant) {
        QueryWrapper entityWrapper = new QueryWrapper();
        String briefName = tenant.getBriefName();
        Integer status = tenant.getStatus();
        String name = tenant.getName();
        String phone = tenant.getPhone();
        Integer type = tenant.getType();
        entityWrapper.eq("del_flag", CommonConstant.STATUS_NORMAL);
        if(StringUtils.isNotBlank(briefName)){
            entityWrapper.like("brief_name", briefName);
        }
        if(StringUtils.isNotBlank(name)){
            entityWrapper.like("name", name);
        }
        if(null != status){
            entityWrapper.eq("status", status);
        }
        if(null != type){
            entityWrapper.eq("type", type);
        }
        if(StringUtils.isNotBlank(phone)){
            entityWrapper.eq("phone", phone);
        }
        return entityWrapper;
    }

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return tenant
     */
    @GetMapping("/{id}")
    public R<Tenant> get(@PathVariable Long id) {
        return R.success(tenantService.getById(id));
    }


    /**
     * 添加
     *
     * @param tenant 实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody Tenant tenant) {
        return R.success(tenantService.save(tenant));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setGmtUpdate(new Date());
        tenant.setDelFlag(CommonConstant.STATUS_DEL);
        return R.success(tenantService.updateById(tenant));
    }

    /**
     * 编辑
     *
     * @param tenant 实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody Tenant tenant) {
        tenant.setGmtUpdate(new Date());
        return R.success(tenantService.updateById(tenant));
    }
}
