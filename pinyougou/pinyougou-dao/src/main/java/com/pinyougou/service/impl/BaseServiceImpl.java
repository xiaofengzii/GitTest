package com.pinyougou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    /**
     * 4.0+之后的版本的spring可以使用泛型依赖注入
     * Mapper<TbBrand> mapper ---->  brandMapper
     */
    @Autowired
    private Mapper<T> mapper;

    @Override
    public T findOne(Serializable id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> findByWhere(T t) {
        return mapper.select(t);
    }

    @Override
    public PageResult findPage(Integer page, Integer pageSize) {
        //设置分页
        PageHelper.startPage(page, pageSize);

        List<T> list = mapper.selectAll();

        //转换为分页信息对象
        PageInfo<T> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public PageResult findPage(Integer page, Integer pageSize, T t) {
        //设置分页
        PageHelper.startPage(page, pageSize);

        List<T> list = mapper.select(t);

        //转换为分页信息对象
        PageInfo<T> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void add(T t) {
        //选择性新增：
        //id name char --- > 只给了id name两个属性值的话；那么如果使用选择性新增则在转换为insert 语句---》insert into tb_brand(id, name) values(?, ?)
        mapper.insertSelective(t);
    }

    @Override
    public void update(T t) {
        //根据主键选择性更新
        mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public void deleteByIds(Serializable[] ids) {
        if (ids != null && ids.length > 0) {
            for (Serializable id : ids) {
                mapper.deleteByPrimaryKey(id);
            }
        }
    }
}
