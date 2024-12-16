package com.cheng.domain.strategy.service.rule.tree.impl;

import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.cheng.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Slf4j
@Component("rule_luck_award")
//幸运奖节点
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {


        log.info("策略树--兜底规则：开始处理");
        //todo 兜底被接管
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVo.TAKE_OVER)
                .strategyAwardData(DefaultTreeFactory.StrategyAwardData.builder()
                        .awardId(100)
                        .awardRuleValue("1,100")
                        .build())
                .build();
    }
}
