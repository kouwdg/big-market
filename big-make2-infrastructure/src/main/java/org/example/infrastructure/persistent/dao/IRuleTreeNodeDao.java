package org.example.infrastructure.persistent.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.RuleTreeNode;

import java.util.List;


@Mapper
public interface IRuleTreeNodeDao {

    String queryRuleLocksCount(@Param("ruleModel") String ruleModel);

    List<RuleTreeNode> queryRuleTreeNodeListByTreeId(@Param("treeId") String treeId);


    //查询TreeId中对应的RuleLocks规则
    List<RuleTreeNode> queryRuleLocks(String[] treeIds);
}
