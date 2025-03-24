package org.example.infrastructure.persistent.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.RuleTreeNodeLine;

import java.util.List;

@Mapper
public interface IRuleTreeNodeLineDao {

    List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(@Param("treeId") String treeId);
}
