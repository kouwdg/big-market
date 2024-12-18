package com.cheng.domain.strategy.service.rule.tree.factory.engine.impl;

import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeNodeLineVO;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeNodeVO;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeVO;
import com.cheng.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.cheng.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.cheng.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
//决策树引擎
public class DecisionTreeEngine implements IDecisionTreeEngine {

    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;
    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }

    @Override
    public DefaultTreeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId) {
        DefaultTreeFactory.StrategyAwardData strategyAwardData = new DefaultTreeFactory.StrategyAwardData();

       log.info("ruleTreeVo:{}",ruleTreeVO);

        //1 根据规则树获得根节点的name
        String next = ruleTreeVO.getTreeRootRuleNode();
        log.info("next:{}",next);
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        while (next!=null){
            //2 根据 根节点的name 获取 根节点的数据
            RuleTreeNodeVO treeNode = treeNodeMap.get(next);

            //3 有了根节点的数据,根据 根结点的name 获取 是否被 接管
            log.info("next:{}",next);
            ILogicTreeNode iLogicTreeNode = logicTreeNodeGroup.get(next);
            DefaultTreeFactory.TreeActionEntity logic = iLogicTreeNode.logic(userId, strategyId, awardId);
            //是否被接管
            RuleLogicCheckTypeVo ruleLogicCheckType = logic.getRuleLogicCheckType();
            strategyAwardData=logic.getStrategyAwardData();
            //根据是否被接管,来决定下一个执行的规则
            next = nextNode(ruleLogicCheckType.getCode(), treeNode.getTreeNodeLineVOList());
        }

        return strategyAwardData;
    }
    public String nextNode(String matterValue, List<RuleTreeNodeLineVO> treeNodeLineVOList) {
        if (null == treeNodeLineVOList || treeNodeLineVOList.isEmpty()) return null;
        for (RuleTreeNodeLineVO nodeLine : treeNodeLineVOList) {
            if (decisionLogic(matterValue, nodeLine)) {
                return nodeLine.getRuleNodeTo();
            }
        }
        return null;
        //throw new RuntimeException("决策树引擎，nextNode 计算失败，未找到可执行节点！");
    }
    public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            // 以下规则暂时不需要实现
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }
    }
}
