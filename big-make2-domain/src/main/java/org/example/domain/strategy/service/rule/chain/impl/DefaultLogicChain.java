package org.example.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IStrategyDispatch;
import org.example.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    protected IStrategyRepository repository;

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Override
    public DefaultChainFactory.StrategyAwardIdVo raffle(String UserId, Long strategyId) {
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链默认处理 userId:{} strategyId:{} ruleModel:{} awardId:{}",
                UserId,strategyId,ruleModel(),awardId);
        return DefaultChainFactory.StrategyAwardIdVo.builder()
                .awardId(awardId)
                .logicModel(ruleModel())
                .build();
    }
    @Override
    protected String ruleModel() {
        return "rule_default";
    }

}
