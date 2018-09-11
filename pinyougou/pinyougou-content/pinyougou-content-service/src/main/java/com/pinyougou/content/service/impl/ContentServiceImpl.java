package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service(interfaceClass = ContentService.class)
public class ContentServiceImpl extends BaseServiceImpl<TbContent> implements ContentService {

    //在redis中内容对应的key
    private static  final  String REDIS_CONTENT ="content";

    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public PageResult search(Integer page, Integer rows, TbContent content) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(content.get***())){
            criteria.andLike("***", "%" + content.get***() + "%");
        }*/

        List<TbContent> list = contentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<TbContent> findContentListByCategoryId(Long categoryId) {
        List<TbContent> list = null;
        try {
            list = (List<TbContent>) redisTemplate.boundHashOps(REDIS_CONTENT).get(categoryId);
            if (list!=null){
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);
        criteria.andEqualTo("status","1");
        example.orderBy("sortOrder").desc();
        list = contentMapper.selectByExample(example);

        try {
            //设置某个分类对应的广告内容列表到缓存中
            redisTemplate.boundHashOps(REDIS_CONTENT).put(categoryId,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public void add(TbContent tbContent){
        super.add(tbContent);
        //更新内容分类到对应的redis中的内容列表缓存中
        updateContentInRedisByCategoryId(tbContent.getCategoryId());
    }

    private void updateContentInRedisByCategoryId(Long categoryId) {
        try {
            redisTemplate.boundHashOps(REDIS_CONTENT).delete(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void update(TbContent tbContent){
        TbContent oldTbContent = super.findOne(tbContent.getId());
        super.update(oldTbContent);
        //是否修改了内容分类,如果修改了内容分类则需要将新旧分类对应的内容类别都得更新
        if(!oldTbContent.getCategoryId().equals(tbContent.getCategoryId())){
            updateContentInRedisByCategoryId(oldTbContent.getCategoryId());
        }
        updateContentInRedisByCategoryId(tbContent.getCategoryId());
    }
    @Override
    public void deleteByIds(Serializable[] ids){
        //1.根据内容id集合查询内容类别,然后再更新该内容分类对应的内容列表缓存
        Example example = new Example(TbContent.class);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        List<TbContent> list = contentMapper.selectByExample(example);
        if (list!=null&&list.size()>0){
            for (TbContent content : list) {
                updateContentInRedisByCategoryId(content.getCategoryId());
            }
        }
        //删除内容
        super.deleteByIds(ids);
    }


}
