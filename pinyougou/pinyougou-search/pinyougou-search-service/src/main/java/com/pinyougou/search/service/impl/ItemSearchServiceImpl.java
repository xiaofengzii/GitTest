package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = ItemSearchService.class)
public class ItemSearchServiceImpl implements ItemSearchService{

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        Map<String, Object> map = new HashMap<>();

        //创建高亮搜索对象
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //设置查询条件
        Criteria criteria = new Criteria("item_keywords")        .is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        if (!StringUtils.isEmpty(searchMap.get("keywords"))){
            searchMap.put("keywords",searchMap.get("keywords").toString().replaceAll(" ",""));
        }
        if (!StringUtils.isEmpty(searchMap.get("category"))){
            Criteria categoryCriteria = new Criteria("item_category").is(searchMap.get("category"));
            SimpleFilterQuery categoryQuery = new SimpleFilterQuery(categoryCriteria);
            query.addFilterQuery(categoryQuery);
        }
        if (!StringUtils.isEmpty(searchMap.get("brand"))){
            Criteria brandCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            SimpleFilterQuery brandQuery = new SimpleFilterQuery(brandCriteria);
            query.addFilterQuery(brandQuery);
        }
        if(searchMap.get("spec")!=null){
            Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
            Set<Map.Entry<String, String>> entries = specMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                Criteria specCriteria = new Criteria("item_spec_" + entry.getKey()).is(entry.getValue());
                SimpleFilterQuery specFilterQuery = new SimpleFilterQuery(specCriteria);
                query.addFilterQuery(specFilterQuery);
            }
        }
        //
        if (!StringUtils.isEmpty(searchMap.get("price"))){
            String[] prices = searchMap.get("price").toString().split("-");
            Criteria greaterThanEqual = new Criteria("item_price").greaterThanEqual(prices[0]);
            SimpleFilterQuery query1 = new SimpleFilterQuery(greaterThanEqual);
            query.addFilterQuery(query);

            if (!"*".equals(prices[1])){
                Criteria lessThanEqual = new Criteria("item_price").lessThanEqual(prices[1]);
                SimpleFilterQuery query2 = new SimpleFilterQuery(lessThanEqual);
                query.addFilterQuery(query2);
            }
        }

        Integer pageNo = 1;
        Integer pageSize = 10;
        if (searchMap.get("pageNo")!=null){
            pageNo = Integer.parseInt(searchMap.get("pageNo").toString());
        }
        if (searchMap.get("pageSize")!=null){
            pageSize = Integer.parseInt(searchMap.get("pageSize").toString());
        }
        query.setOffset(pageNo);
        query.setRows(pageSize);

        //设置高亮
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");
        //设置高亮起始标签
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");// 高亮结束标签
        query.setHighlightOptions(highlightOptions);

        //设置拍寻
        if (!StringUtils.isEmpty(searchMap.get("sortField")) && !StringUtils.isEmpty(searchMap.get("sort"))){
            String sortOrder = searchMap.get("sort").toString();
            Sort sort = new Sort(sortOrder.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC,"item_"+searchMap.get("sortField").toString());
            query.addSort(sort);
        }

        //查询
        HighlightPage<TbItem> itemHighlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //处理高亮标题
        List<HighlightEntry<TbItem>> highlighted = itemHighlightPage.getHighlighted();
        if ( highlighted!=null&&highlighted.size()>0){
            for (HighlightEntry<TbItem> entry : highlighted) {
                List<HighlightEntry.Highlight> highlights = entry.getHighlights();
                if (highlights != null && highlights.size() > 0 &&
                        highlights.get(0).getSnipplets() != null) {
                    //设置高亮标题
                    entry.getEntity().setTitle(highlights.get(0).getSnipplets().get(0));
                }
            }

        }
        map.put("rows",itemHighlightPage.getContent());
        map.put("totalPages",itemHighlightPage.getTotalPages());
        map.put("total", itemHighlightPage.getTotalElements());
        return map;
    }

    @Override
    public void importItemList(List<TbItem> itemList) {
        for (TbItem item : itemList) {
            Map specMap = JSON.parseObject(item.getSpec(), Map.class);
            item.setSpecMap(specMap);
        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    @Override
    public void deleteItemByGoodsIdList(List<Long> goodsIdList) {
        Criteria criteria = new Criteria("item_goodsid").in(goodsIdList);
        SimpleQuery query = new SimpleQuery(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
