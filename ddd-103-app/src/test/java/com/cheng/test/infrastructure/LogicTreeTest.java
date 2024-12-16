package com.cheng.test.infrastructure;
import com.alibaba.fastjson2.JSON;
import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleLimitTypeVO;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeNodeLineVO;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeNodeVO;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeVO;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.cheng.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 规则树测试
 * @create 2024-01-27 13:23
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogicTreeTest {
    @Resource
    private DefaultTreeFactory defaultTreeFactory;

    @Resource
    private IStrategyRepository repository;
    /**
     * rule_lock --左--> rule_luck_award
     *           --右--> rule_stock --右--> rule_luck_award
     */
    @Test
    public void test_tree_rule() {
        // 构建参数
        RuleTreeNodeVO rule_lock = RuleTreeNodeVO.builder()
                .treeId("100000001")
                .ruleKey("rule_lock")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {{
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("100000001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVo.TAKE_OVER)
                            .build());
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("100000001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_stock")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVo.ALLOW)
                            .build());
                }})
                .build();
        RuleTreeNodeVO rule_luck_award = RuleTreeNodeVO.builder()
                .treeId("100000001")
                .ruleKey("rule_luck_award")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(null)
                .build();
        RuleTreeNodeVO rule_stock = RuleTreeNodeVO.builder()
                .treeId("100000001")
                .ruleKey("rule_stock")
                .ruleDesc("库存处理规则")
                .ruleValue(null)
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {{
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("100000001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVo.TAKE_OVER)
                            .build());
                }})
                .build();
        RuleTreeVO ruleTreeVO = new RuleTreeVO();
        ruleTreeVO.setTreeId("100000001");
        ruleTreeVO.setTreeName("决策树规则；增加dall-e-3画图模型");
        ruleTreeVO.setTreeDesc("决策树规则；增加dall-e-3画图模型");
        //设置规则根节点
        ruleTreeVO.setTreeRootRuleNode("rule_lock");
        ruleTreeVO.setTreeNodeMap(new HashMap<String, RuleTreeNodeVO>() {{
            put("rule_lock", rule_lock);
            put("rule_stock", rule_stock);
            put("rule_luck_award", rule_luck_award);
        }});
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        DefaultTreeFactory.StrategyAwardData data = treeEngine.process("user3", 10001L, 100);
        log.info("测试结果：{}", JSON.toJSONString(data));
    }


    @Test
    public void test2(){
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVoByTreeId("tree_lock");
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();
        Set<String> strings = treeNodeMap.keySet();
        log.info("节点");
        for (String s : strings) {
            RuleTreeNodeVO treeNodeVO = treeNodeMap.get(s);
            log.info("节点：{}",treeNodeVO);
        }
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        DefaultTreeFactory.StrategyAwardData data = treeEngine.process("user3", 10001L, 100);
        log.info("测试结果：{}", JSON.toJSONString(data));

    }
}