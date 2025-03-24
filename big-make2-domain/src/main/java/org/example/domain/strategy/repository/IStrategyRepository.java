package org.example.domain.strategy.repository;


import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.model.vo.RuleWeightVo;
import org.example.domain.strategy.model.vo.StrategyAwardRuleModelVo;
import org.example.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import org.example.domain.strategy.model.vo.ruleTree.RuleTreeVO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IStrategyRepository {

    //根据strategyId查询strategy_award中奖品数据
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);


    //存入redis中
    public void storeStrategyAwardSwarchRateTables(String key, BigDecimal rateRange, HashMap hashMap);

    //查询strategy_rule表格的数据
    List<StrategyRuleEntity> queryStrategyRuleByStrategyId(Long strategyId);

    StrategyEntity queryStrategyByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRuleByStrategyIdAndRuleModel(Long strategyId, String ruleWeight);


    //查询StrategyRule中的ruleValue值
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    //查询StrategyRule中的ruleValue值
    String queryStrategyRuleValue(Long strategyId, String s);


    int getRateRange(Long strategyId);

    int getStrategyAwardAssemble(Long strategyId, int i);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey,String ruleWeightValue);

    String getScoreRange(Long strategyId, Long userScore);


    int getRuleWeightRateRange(Long strategyId, String ruleWeightValue);


    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    //查看奖品加锁次数
    Integer queryAwardLockCount(Long strategyId, Integer awardId);


    //根据treeId查询并格式化 ruleTree
    public RuleTreeVO queryRuleTreeVoByTreeId(String treeId);

    //扣减库存
    Boolean AbatementAwardStock(String cacheKey);

    //向redis中缓存 奖品数量
    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    //写入延迟队列
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVo build);

    //获得延迟队列
    StrategyAwardStockKeyVo takeQueryValue();

    //更新数据库奖品库存
    void updateStrategyAwardStock(Long strategy, Integer award);

    //根据活动ID查询策略Id
    Long queryStrategyIdByActivityId(Long activityId);


    //查询奖品加锁情况
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);

    //查询奖品的具体信息
    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);

    StrategyAwardRuleModelVo queryStrategyAwardRuleModel(Long strategyId, Integer awardId);

    //查询 ruleWeight的信息
    List<RuleWeightVo> queryAwardRuleWeight(Long strategyId);
    //查询分级
    String getScoreRangeByCount(Long strategyId, Integer count);


}
