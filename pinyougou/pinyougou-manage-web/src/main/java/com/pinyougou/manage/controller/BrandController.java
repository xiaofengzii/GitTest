package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/brand")
//@Controller
@RestController //组合注解；包括了Controller ResponseBody两个注解；对该类的所有方法生效
public class BrandController {

    /**
     * 从注册中心获取该对象；在配置文件中已经指定了注册中心
     */
    @Reference(timeout = 10000)
    private BrandService brandService;

    /**
     * 获取并输出所有品牌列表
     * @return
     */
    @GetMapping("/findAll")
    //@RequestMapping(value = "/findAll", method =  RequestMethod.GET)
    //@ResponseBody
    public List<TbBrand> findAll(){
        //return brandService.queryAll();
        return brandService.findAll();
    }

    /**
     * 根据分页信息分页查询品牌数据
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    @GetMapping("/testPage")
    public List<TbBrand> testPage(@RequestParam(value="page", defaultValue = "1")Integer page,
                                  @RequestParam(value="rows", defaultValue = "5")Integer rows){
        //return brandService.testPage(page, rows);
        return (List<TbBrand>) brandService.findPage(page, rows).getRows();
    }

    /**
     * 根据分页信息分页查询品牌数据
     * @param page 页号
     * @param rows 页大小
     * @return 分页对象
     */
    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(value="page", defaultValue = "1")Integer page,
                               @RequestParam(value="rows", defaultValue = "5")Integer rows){
        return brandService.findPage(page, rows);
    }

    /**
     * 新增品牌
     * @param brand 品牌
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody TbBrand brand){
        try {
            brandService.add(brand);

            return Result.ok("新增品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("新增品牌失败");
    }

    /**
     * 根据id查询品牌
     * 在方法中的请求参数名称要与请求路径携带的参数名称一致；如果想不一致的话可以使用@RequestParam()进行修改
     * @param id 品牌id
     * @return 品牌
     */
    @GetMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }


    /**
     * 更新品牌
     * @param brand 品牌
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody TbBrand brand){
        try {
            brandService.update(brand);

            return Result.ok("更新品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("更新品牌失败");
    }

    /**
     * 批量删除品牌
     * @param ids 品牌id集合
     * @return 操作结果
     */
    @GetMapping("/delete")
    public Result delete(Long[] ids){
        try {
            brandService.deleteByIds(ids);
            return Result.ok("删除品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除品牌失败");
    }

    /**
     * 根据条件分页查询
     * @param brand 查询条件对象
     * @param page 页号
     * @param rows 页大小
     * @return 分页结果
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody TbBrand brand,
                             @RequestParam(value="page", defaultValue = "1")Integer page,
                             @RequestParam(value="rows", defaultValue = "5")Integer rows){
        return brandService.search(brand, page, rows);
    }

    /**
     * 查询数据库中的所有品牌；并返回一个集合，集合中的数据结构如下：
     *
     * @return [{id:'1',text:'联想'},{id:'2',text:'华为'}]
     */
    @GetMapping("selectOptionList")
    public List<Map<String,Object>> selectOptionList(){
        return brandService.selectOptionList();
    }

}
