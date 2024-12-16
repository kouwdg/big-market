package com.cheng.domain.strategy.service.rule.tree.impl;

import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.armory.IStrategyDispatch;
import com.cheng.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.cheng.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyDispatch dispatch;

    @Resource
    private IStrategyRepository repository;
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        log.info("策略树--库存规则：开始处理");

        log.info("开始扣减库存: userId:{},awardId:{} strategyId:{}",userId,awardId,strategyId);
        Boolean status = dispatch.subtractionAwardStock(strategyId, awardId);
        if(!status){
            log.info("库存扣减失败: userId:{},awardId:{} strategyId:{}",userId,awardId,strategyId);
            //todo 库存不足 接管
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVo.TAKE_OVER)
                    .build();
        }

        //todo 写入延迟队列，延迟队列消费更新数据库记录
        repository.awardStockConsumeSendQueue(StrategyAwardStockKeyVo.builder()
                .awardId(awardId)
                .strategyId(strategyId)
                .build());

        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVo.ALLOW)
                .strategyAwardData(DefaultTreeFactory.StrategyAwardData.builder()
                        .awardId(awardId)
                        .awardRuleValue("")
                        .build())
                .build();

    }
}
