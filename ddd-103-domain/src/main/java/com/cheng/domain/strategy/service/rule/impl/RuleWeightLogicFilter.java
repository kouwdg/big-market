package com.cheng.domain.strategy.service.rule.impl;

import com.cheng.domain.strategy.model.entity.RuleActionEntity;
import com.cheng.domain.strategy.model.entity.RuleMatterEntity;
import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.annotation.LogicStrategy;
import com.cheng.domain.strategy.service.rule.ILogicFilter;
import com.cheng.domain.strategy.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


//权重抽奖

@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {


    //抽奖接口
    @Resource
    private IStrategyRepository repository;


    //todo 先暂时手动设置幸运分
    private Long userScore=5200L;


    //权重抽奖
    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {

        // 1 看幸运值应该使用哪个范围
        String ruleWeightValue=repository.getScoreRange(ruleMatterEntity.getStrategyId(),userScore);

        if (ruleWeightValue==null){
            //没有达到幸运值要求
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVo.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVo.ALLOW.getInfo())
                    .build();
        }

        //达到了幸运值的要求
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .ruleModel(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode())
                .data(
                        RuleActionEntity.RaffleBeforeEntity.builder()
                                .strategyId(ruleMatterEntity.getStrategyId())
                                .ruleWeightValue(ruleWeightValue)
                                .build())
                .code(RuleLogicCheckTypeVo.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVo.TAKE_OVER.getInfo())
                .build();
    }


}
