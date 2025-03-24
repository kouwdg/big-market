package org.example.domain.strategy.service.rule;

import org.example.domain.strategy.model.vo.RuleWeightVo;

import java.util.List;
import java.util.Map;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/28 16:50
 */
public interface IRaffleRule {


    //查询奖品的加锁情况
    Map<String,Integer> queryAwardRuleLockCount(String[]treeIds);

    //获得奖品的加锁次数
    Integer queryAwardLockCount(Long strategyId,Integer awardId);

    List<RuleWeightVo> queryAwardRuleWeight(Long strategyId);
    List<RuleWeightVo> queryAwardRuleWeightByActivityId(Long activityId);
}
