package org.example.domain.strategy.service.rule.tree.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component("rule_lock")
//加锁节点
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyRepository strategyRepository;
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        log.info("策略树--次数锁规则：开始处理");

        //todo 获得抽奖次数
        int count=3;

        // 查看奖品加锁次数
        Integer lockCount= strategyRepository.queryAwardLockCount(strategyId,awardId);
        log.info("奖品加锁次数：{}",lockCount);
        //放行
        if (count>=lockCount){
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVo.ALLOW)
                    .build();
        }
        //接管
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVo.TAKE_OVER)
                .build();
    }
}
