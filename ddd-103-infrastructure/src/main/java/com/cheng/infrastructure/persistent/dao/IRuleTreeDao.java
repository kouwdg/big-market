package com.cheng.infrastructure.persistent.dao;


import com.cheng.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IRuleTreeDao {
    RuleTree queryRuleTreeByTreeId(@Param("treeId") String treeId);
}
