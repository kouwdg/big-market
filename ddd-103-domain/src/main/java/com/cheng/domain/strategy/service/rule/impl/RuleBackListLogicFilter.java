package com.cheng.domain.strategy.service.rule.impl;

import com.cheng.domain.strategy.model.entity.RuleActionEntity;
import com.cheng.domain.strategy.model.entity.RuleMatterEntity;
import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.annotation.LogicStrategy;
import com.cheng.domain.strategy.service.rule.ILogicFilter;
import com.cheng.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.cheng.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//对黑名单规则进行处理
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository repository;
    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-黑名单 userId:{} strategyId:{} ruleModel:{}",
                ruleMatterEntity.getUserId(),ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel());

        String userId=ruleMatterEntity.getUserId();

        //查询出规则value
        String ruleValue=repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),ruleMatterEntity.getAwardId(),ruleMatterEntity.getRuleModel());

        String[] split = ruleValue.split(Constants.COLON);
        int awardId = Integer.parseInt(split[0]);

        //获得黑名单中的用户信息
        String[] blackUsers = split[1].split(Constants.SPLIT);
        for (String blackUser : blackUsers) {
            if(userId.equals(blackUser)){
                //接管
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(
                                RuleActionEntity.RaffleBeforeEntity.builder()
                                        .strategyId(ruleMatterEntity.getStrategyId())
                                        .awardId(awardId)
                                        .build())
                        .code(RuleLogicCheckTypeVo.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVo.TAKE_OVER.getInfo())
                        .build();
            }
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVo.ALLOW.getCode())
                .info(RuleLogicCheckTypeVo.ALLOW.getInfo())
                .build();
    }
}
