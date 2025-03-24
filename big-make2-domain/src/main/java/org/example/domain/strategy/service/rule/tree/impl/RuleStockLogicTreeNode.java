package org.example.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IStrategyDispatch;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {
    @Resource
    private IStrategyDispatch strategyDispatch;
    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        log.info("策略树--库存规则：开始处理");

        log.info("开始扣减库存");
        //扣减库存
        Boolean status = strategyDispatch.AbatementAwardStock(strategyId, awardId);
        if (!status) {
            //库存不足 接管
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVo.TAKE_OVER)
                    .build();
        }

        //写入延迟队列 等待数据库处理奖品库存
        strategyRepository.awardStockConsumeSendQueue(
                StrategyAwardStockKeyVo.builder()
                        .awardId(awardId)
                        .strategyId(strategyId)
                        .build()
        );
        //默认放行
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVo.ALLOW)
                .build();
    }
}
