package org.example.domain.award.service.distribute;

import org.example.domain.award.model.entity.DistributeAwardEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 奖品发放接口
 * @date 2025/3/27 8:30
 */
public interface IAwardDistributionService {

    String queryAwardKey(Integer awardId);


    //发放奖品
    void distributeAward(DistributeAwardEntity distributeAward);
}
