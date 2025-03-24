package org.example.domain.strategy.service.rule.chain.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.strategy.model.entity.ActivityAccountEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IStrategyDispatch;
import org.example.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {
    @Resource
    private IStrategyRepository repository;

    @Resource
    private IActivityRepository activityRepository;
    @Resource
    private IStrategyDispatch dispatch;
    //todo 先暂时手动设置幸运分
    private Long userScore=1000L;

//    @Override
//    public DefaultChainFactory.StrategyAwardIdVo raffle(String UserId, Long strategyId) {
//        log.info("抽奖责任链-权重开始 userId:{} strategyId:{} ruleModel:{}", UserId,strategyId,ruleModel());
//
//        String scoreRange = repository.getScoreRange(strategyId, userScore);
//        if (scoreRange==null){
//            log.info("抽奖责任链-权重放行 userId:{} strategyId:{} ruleModel:{}", UserId,strategyId,ruleModel());
//            return Next().raffle(UserId,strategyId);
//        }
//        //抽奖
//        Integer awardId = dispatch.getRandomAwardId(strategyId, scoreRange);
//        log.info("抽奖责任链-权重接管 scoreRange:{}",scoreRange);
//        return DefaultChainFactory.StrategyAwardIdVo.builder()
//                .awardId(awardId)
//                .logicModel(ruleModel())
//                .build();
//    }

    @Override
    protected String ruleModel() {
        return "rule_weight";
    }


    @Override
    public DefaultChainFactory.StrategyAwardIdVo raffle(String userId, Long strategyId) {
        //1 查询抽奖次数
            //todo 暂时设置 活动id是固定的
        ActivityAccountEntity entity = activityRepository.queryActivityAccountByUserId(userId, 101L);
        int count=entity.getTotalCount()-entity.getTotalCountSurplus();
        log.info("cx001   抽奖次数{}",count);

        //寻找分级
        String scoreRange=repository.getScoreRangeByCount(strategyId,count);
        if (scoreRange==null){
           log.info("抽奖责任链-权重放行 userId:{} strategyId:{} ruleModel:{}", userId,strategyId,ruleModel());
           return Next().raffle(userId,strategyId);
       }
        Integer awardId = dispatch.getRandomAwardId(strategyId, scoreRange);
        log.info("抽奖责任链-权重接管 scoreRange:{}",scoreRange);
        return DefaultChainFactory.StrategyAwardIdVo.builder()
                .awardId(awardId)
                .logicModel(ruleModel())
                .build();
    }
}
