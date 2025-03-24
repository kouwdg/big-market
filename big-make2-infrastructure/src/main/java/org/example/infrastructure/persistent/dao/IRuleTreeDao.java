package org.example.infrastructure.persistent.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.RuleTree;

@Mapper
public interface IRuleTreeDao {
    RuleTree queryRuleTreeByTreeId(@Param("treeId") String treeId);
}
