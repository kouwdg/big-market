package org.example.infrastructure.persistent.dao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.StrategyAward;
import java.util.List;

@Mapper
public interface IStrategyAwardDao {
    List<StrategyAward> queryAll();

    List<StrategyAward> queryByStrategyId(@Param("strategyId") Long strategyId);

    String queryRuleModel(@Param("strategyId") Long strategyId,@Param("awardId") Integer awardId);

    //自减库存
    void DescAwardStock(StrategyAward strategyAward);


    //根据strategyId查询对应的奖品列表
    List<StrategyAward> queryStrategyAwardListByStrategyId(@Param("strategyId") Long strategyId);

    StrategyAward queryStrategyAward(StrategyAward strategyAward);

    String queryStrategyAwardRuleModel(StrategyAward strategyAward);
}
