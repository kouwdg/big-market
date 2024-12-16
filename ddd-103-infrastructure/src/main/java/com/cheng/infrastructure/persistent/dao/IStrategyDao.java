package com.cheng.infrastructure.persistent.dao;

import com.cheng.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IStrategyDao {
    Strategy queryStrategyByStrategyId(@Param("strategyId") Long strategyId);
}
