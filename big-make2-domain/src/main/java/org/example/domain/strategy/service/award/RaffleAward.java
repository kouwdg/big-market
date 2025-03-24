package org.example.domain.strategy.service.award;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/18 11:19
 */
@Service
@Slf4j
public class RaffleAward implements IRaffleAward{

    @Resource
    private IStrategyRepository strategyRepository;
    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return strategyRepository.queryStrategyAwardList(strategyId);
    }

    /**
     * 根据活动Id查询奖品列表
     * @param ActivityId
     * @return
     */
    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long ActivityId) {
        Long strategyId = strategyRepository.queryStrategyIdByActivityId(ActivityId);
        log.info("strategyId:{}",strategyId);
        return queryRaffleStrategyAwardList(strategyId);
    }
}
