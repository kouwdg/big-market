package org.example.domain.strategy.service.rule;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.RuleWeightVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/18 19:37
 */
@Service
@Slf4j
public class RaffleRule implements IRaffleRule{
    @Resource
    private IStrategyRepository strategyRepository;
    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        return strategyRepository.queryAwardRuleLockCount(treeIds);
    }

    @Override
    public Integer queryAwardLockCount(Long strategyId, Integer awardId) {
        return null;
    }


    //查询 ruleWeight的信息
    @Override
    public List<RuleWeightVo> queryAwardRuleWeight(Long strategyId) {
        return strategyRepository.queryAwardRuleWeight(strategyId);
    }

    @Override
    public List<RuleWeightVo> queryAwardRuleWeightByActivityId(Long activityId) {
        Long strategyId= strategyRepository.queryStrategyIdByActivityId(activityId);
      return   queryAwardRuleWeight(strategyId);
    }
}
