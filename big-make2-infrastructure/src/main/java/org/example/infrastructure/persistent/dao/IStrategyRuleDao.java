package org.example.infrastructure.persistent.dao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.StrategyRule;

import java.util.List;


@Mapper
public interface IStrategyRuleDao {
    List<StrategyRule> queryStrategyRuleByStrategyId(Long strategyId);


    StrategyRule queryStrategyRuleByStrategyIdAndRuleModel(@Param("strategyId") Long strategyId,@Param("ruleModel") String ruleModel);

    StrategyRule queryStrategyRuleValue(StrategyRule rule);
}
