package com.edu.sky.promotion.po.dao;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<T, E, PK extends Serializable> {
    long countByExample(E example);

    int deleteByExample(E example);

    int insert(T record);

    int insertSelective(T record);

    List<T> selectByExample(E example);

    int updateByExampleSelective(@Param("record") T record, @Param("example") E example);

    int updateByExample(@Param("record") T record, @Param("example") E example);

    List<T> selectByPage(@Param("example") E example, @Param("offset") int offset, @Param("limit") int limit);

}
