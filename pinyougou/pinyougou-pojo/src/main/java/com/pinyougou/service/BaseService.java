package com.pinyougou.service;

import com.pinyougou.vo.PageResult;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {
    public T findOne(Serializable id);

    public List<T> findAll();

    public List<T> findByWhere(T t);

    public PageResult findPage(Integer page, Integer rows);

    public PageResult findPage(Integer page,Integer rows,T t);
    public void add(T t);
    /**
     *  根据主键更新
     * @param t  实体对象
     */
    public void update(T t);
    /**
     *  批量删除
     * @param ids  主键集合
     */
    public void deleteByIds(Serializable[] ids);
}
