package com.cheng.infrastructure.persistent.dao;

import com.cheng.domain.strategy.model.entity.StrategyAwardEntity;
import com.cheng.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IStrategyAwardDao {
    List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(@Param("strategyId") Long strategyId);

    String queryStrategyAwardRuleModel(StrategyAward strategyAward);

    //更新库存
    void updateStrategyAwardStock(StrategyAward strategyAward);

    StrategyAward queryStrategyAward(StrategyAward strategyAward);
}
