package com.cheng.test.infrastructure;

import com.cheng.infrastructure.persistent.dao.IRuleTreeDao;
import com.cheng.infrastructure.persistent.dao.IRuleTreeNodeDao;
import com.cheng.infrastructure.persistent.dao.IRuleTreeNodeLineDao;
import com.cheng.infrastructure.persistent.po.RuleTree;
import com.cheng.infrastructure.persistent.po.RuleTreeNode;
import com.cheng.infrastructure.persistent.po.RuleTreeNodeLine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class daoTest {

    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Test
    public void test1(){
        List<RuleTreeNodeLine> treeLock2 = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId("tree_lock");
        for (RuleTreeNodeLine nodeLine : treeLock2) {
            System.out.println(nodeLine);
        }

        List<RuleTreeNode> treeLock1 = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId("tree_lock");
        for (RuleTreeNode ruleTreeNode : treeLock1) {
            System.out.println(ruleTreeNode);
        }
        RuleTree treeLock = ruleTreeDao.queryRuleTreeByTreeId("tree_lock");
        System.out.println(treeLock);
    }
}
