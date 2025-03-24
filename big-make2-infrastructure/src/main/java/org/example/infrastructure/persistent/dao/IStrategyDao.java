package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.Strategy;


@Mapper
public interface IStrategyDao {

    Strategy queryByStrategyId(@Param("strategyId") Long strategyId);
}
