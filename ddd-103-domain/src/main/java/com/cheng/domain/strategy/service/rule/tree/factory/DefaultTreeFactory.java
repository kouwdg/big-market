package com.cheng.domain.strategy.service.rule.tree.factory;


import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeVO;
import com.cheng.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.cheng.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.cheng.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


//规则树工厂
@Service
public class DefaultTreeFactory {
    private final Map<String,ILogicTreeNode> logicTreeNodeGroup;

    @Autowired
    public DefaultTreeFactory(Map<String, ILogicTreeNode>logicTreeNodeGroup){
        this.logicTreeNodeGroup=logicTreeNodeGroup;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }



    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity{
        private RuleLogicCheckTypeVo ruleLogicCheckType;
        private StrategyAwardData strategyAwardData;

    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardData{
        private Integer awardId;
        private String awardRuleValue;
    }
}
