package com.cheng.infrastructure.persistent.dao;

import com.cheng.infrastructure.persistent.po.Strategy;
import com.cheng.infrastructure.persistent.po.strategyRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IStrategyRuleDao {
    strategyRule queryStrategyRuleByStrategyIdAndRuleModel(@Param("strategyId") Long strategyId,@Param("ruleModel") String ruleModel);

    strategyRule queryStrategyRuleValue(strategyRule strategyRule);
}
