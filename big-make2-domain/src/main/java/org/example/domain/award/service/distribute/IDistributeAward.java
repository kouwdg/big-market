package org.example.domain.award.service.distribute;

import org.example.domain.award.model.entity.DistributeAwardEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 分发类型奖品的接口
 * @date 2025/3/25 17:05
 */
public interface IDistributeAward {

    //
    void giveOutPrizes(DistributeAwardEntity entity);

    //配送发奖奖品
    void distributeAward(DistributeAwardEntity entity);
}
