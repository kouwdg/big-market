package com.cheng.domain.strategy.service.rule.tree.factory.engine;

import com.cheng.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardData process(String userId,Long strategyId,Integer awardId);
}
