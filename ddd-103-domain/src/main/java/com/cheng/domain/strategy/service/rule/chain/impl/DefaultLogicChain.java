package com.cheng.domain.strategy.service.rule.chain.impl;

import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.cheng.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    protected IStrategyRepository repository;
    @Override
    public DefaultChainFactory.StrategyAwardIdVo logic(String UserId, Long strategyId) {
        //获得最大值
        int rateRange = repository.getRateRange(strategyId);
        log.info("rateRange：{}",rateRange);
        int i = new Random().nextInt(rateRange);
        log.info("生成的key:{}",i);
        int awardId = repository.getStrategyAwardAssemble(strategyId,i);
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
