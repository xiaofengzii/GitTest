package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ItemController {
    /*
    http://item.pinyougou.com/{{item.goodsId}}.html
    * */
    @Reference
    private GoodsService goodsService;
    @Reference
    private ItemCatService itemCatService;

    @GetMapping("/{goodsId}")
    public ModelAndView toItemPage(@PathVariable Long goodsId){
        ModelAndView mv = new ModelAndView("item");
        Goods goods = goodsService.findGoodsByIdAndStatus(goodsId,"1");
        mv.addObject("goods",goods.getGoods());
        mv.addObject("goodsDesc",goods.getGoodsDesc());
        mv.addObject("itemList",goods.getItemList());
        TbItemCat one = itemCatService.findOne(goods.getGoods().getCategory1Id());
        mv.addObject("itemCat1",one.getName());
        TbItemCat two = itemCatService.findOne(goods.getGoods().getCategory2Id());
        mv.addObject("itemCat2",two.getName());
        TbItemCat three = itemCatService.findOne(goods.getGoods().getCategory3Id());
        mv.addObject("itemCat3",three.getName());

        return mv;
    }


}
