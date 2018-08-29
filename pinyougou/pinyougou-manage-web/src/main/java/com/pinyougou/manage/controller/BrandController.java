package com.pinyougou.manage.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/brand")
//@Controller
@RestController //组合注解；包括了Controller ResponseBody两个注解；对该类的所有方法生效
public class BrandController {

    /**
     * 从注册中心获取该对象；在配置文件中已经指定了注册中心
     */
    @Reference
    private BrandService brandService;

    @PostMapping("search")
    public PageResult search(@RequestBody TbBrand tbBrand,@RequestParam(value = "page", defaultValue = "1")Integer page,
                             @RequestParam(value = "rows", defaultValue = "10")Integer rows){
       return brandService.search(tbBrand,page,rows);
    }

    @GetMapping("delete")
    public Result delete(Long[] ids){
        try {
            brandService.deleteByIds(ids);
            return Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    @PostMapping("update")
    public Result update(@RequestBody TbBrand tbBrand){
        try {
            brandService.update(tbBrand);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  Result.fail("修改失败");
    }

    //修改中的查找一个品牌方法,数据要显示在弹窗
    @PostMapping("findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }

    //新建方法
    @PostMapping("add")
    public Result add(@RequestBody TbBrand tbBrand){
        try {
            brandService.add(tbBrand);
            return Result.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("新增失败");
    }

    @GetMapping("findPage")
    public PageResult findPage(@RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows){
        return brandService.findPage(page, rows);
    }

//    http://localhost:9100/brand/testPage.do?page=1&rows=5
    @GetMapping("/testPage")
    public List<TbBrand> testPage(Integer page , Integer rows){

        return (List<TbBrand>) brandService.findPage(page,rows).getRows();
    }

    @GetMapping("/findAll")
    //@RequestMapping(value = "/findAll", method =  RequestMethod.GET)
    //@ResponseBody
    public List<TbBrand> findAll(){

        return brandService.findAll();
    }
}
