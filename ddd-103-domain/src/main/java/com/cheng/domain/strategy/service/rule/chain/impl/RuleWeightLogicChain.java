package com.cheng.domain.strategy.service.rule.chain.impl;

import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.armory.IStrategyDispatch;
import com.cheng.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.cheng.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {
    @Resource
    private IStrategyRepository repository;
    @Resource
    private IStrategyDispatch dispatch;
    //todo 先暂时手动设置幸运分
    private Long userScore=5200L;

    @Override
    public DefaultChainFactory.StrategyAwardIdVo logic(String UserId, Long strategyId) {
        log.info("抽奖责任链-权重开始 userId:{} strategyId:{} ruleModel:{}", UserId,strategyId,ruleModel());

        String scoreRange = repository.getScoreRange(strategyId, userScore);
        if (scoreRange==null){
            log.info("抽奖责任链-权重放行 userId:{} strategyId:{} ruleModel:{}", UserId,strategyId,ruleModel());
            return next().logic(UserId,strategyId);
        }
        //抽奖
        Integer awardId = dispatch.getRandomAwardId(strategyId, scoreRange);
        log.info("抽奖责任链-权重接管 userId:{} strategyId:{} ruleModel:{} awardId:{}",
                UserId,strategyId,ruleModel(),awardId);
        return DefaultChainFactory.StrategyAwardIdVo.builder()
                .awardId(awardId)
                .logicModel(ruleModel())
                .build();
    }

    @Override
    protected String ruleModel() {
        return "rule_weight";
    }
}
