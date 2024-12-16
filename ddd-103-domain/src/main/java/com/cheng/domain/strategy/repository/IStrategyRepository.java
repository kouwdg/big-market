package com.cheng.domain.strategy.repository;


import com.cheng.domain.strategy.model.entity.StrategyAwardEntity;
import com.cheng.domain.strategy.model.entity.StrategyEntity;
import com.cheng.domain.strategy.model.entity.StrategyRuleEntity;
import com.cheng.domain.strategy.model.vo.StrategyAwardRuleModelVo;
import com.cheng.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeVO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSwarchRateTables(String strategyId, BigDecimal rateRange, HashMap<Object, Object> objectObjectHashMap);

    int getRateRange(Long strategyId);

    int getRuleWeightRateRange(Long strategyId,String ruleWeightValue);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey);
    Integer getStrategyAwardAssemble(Long strategyId, int rateKey,String ruleWeightValue);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRuleByStrategyIdAndRuleModel(Long strategyId, String ruleWeight);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);
    String queryStrategyRuleValue(Long strategyId,String ruleModel);


    String getScoreRange(Long strategyId,Long userSoure);

    StrategyAwardRuleModelVo queryStrategyAwardRuleModel(Long strategyId, Integer awardId);

    RuleTreeVO queryRuleTreeVoByTreeId(String treeId);


    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);


    Boolean subtractionAwardStock(String cacheKey);

    //写入延迟队列
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVo build);

    //获取队列值
    StrategyAwardStockKeyVo takeQueryValue();


    void updateStrategyAwardStock(Long strategy, Integer award);

    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);
}
