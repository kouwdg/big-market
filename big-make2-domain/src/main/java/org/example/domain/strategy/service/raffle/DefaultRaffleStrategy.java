package org.example.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.StrategyAwardRuleModelVo;
import org.example.domain.strategy.model.vo.ruleTree.RuleTreeVO;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.example.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy{

    @Override
    protected DefaultChainFactory.StrategyAwardIdVo raffleLogicChain(String userId, Long StrategyId) {
        ILogicChain iLogicChain = defaultChainFactory.openLogicChain(StrategyId);
        return iLogicChain.raffle(userId,StrategyId);
    }

    @Override
    protected DefaultTreeFactory.StrategyAwardData raffleLogicTree(String userId, Long StrategyId, Integer awardId) {
        //查看该奖品是否有 规则树
        StrategyAwardRuleModelVo strategyAwardRuleModelVo =
                repository.queryStrategyAwardRuleModel(StrategyId, awardId);
        if (strategyAwardRuleModelVo==null){
            log.info("奖品没有设置规则树");
            return DefaultTreeFactory.StrategyAwardData.builder().awardId(awardId).build();
        }
        log.info("奖品对应的规则：{}",strategyAwardRuleModelVo.getRuleModel());
        RuleTreeVO ruleTreeVO=
                repository.queryRuleTreeVoByTreeId(strategyAwardRuleModelVo.getRuleModel());

        log.info("工厂装配的 RuleTreeVo：{}",ruleTreeVO);
        IDecisionTreeEngine engine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return engine.process(userId, StrategyId, awardId);
    }
}
