package org.example.domain.strategy.service.rule.chain;

import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 责任链接口
 * @date 2025/2/15 12:25
 */
public interface ILogicChain {

    public ILogicChain addNext(ILogicChain next);
    public ILogicChain Next();
    public DefaultChainFactory.StrategyAwardIdVo raffle(String userId,Long strategyId);
}
