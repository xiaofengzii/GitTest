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
    public PageResult findPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        List<T> selectAll = mapper.selectAll();
        PageInfo<T> info = new PageInfo<T>(selectAll);
        return new PageResult(info.getTotal(),info.getList());
    }

    @Override
    public PageResult findPage(Integer page, Integer rows, T t) {
        PageHelper.startPage(page,rows);
        List<T> selectAll = mapper.select(t);
        PageInfo<T> info = new PageInfo<T>(selectAll);
        return new PageResult(info.getTotal(),info.getList());
    }

    @Override
    public void add(T t) {
        mapper.insertSelective(t);
    }

    /**
     * 根据主键更新
     *
     * @param t 实体对象
     */
    @Override
    public void update(T t) {
        mapper.updateByPrimaryKeySelective(t);
    }

    /**
     * 批量删除
     *
     * @param ids 主键集合
     */
    @Override
    public void deleteByIds(Serializable[] ids) {
        if (ids!=null&&ids.length>0){
            for (Serializable id : ids) {
                mapper.deleteByPrimaryKey(id);
            }
        }
    }
}
