package com.cheng.domain.strategy.service.rule.chain;


import com.cheng.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

//责任链接口
public interface ILogicChain {


    //返回奖品Id
    DefaultChainFactory.StrategyAwardIdVo logic(String UserId, Long strategyId);

    ILogicChain appendNext(ILogicChain next);

    ILogicChain next();

}
