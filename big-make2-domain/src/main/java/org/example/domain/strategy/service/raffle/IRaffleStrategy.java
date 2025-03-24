package org.example.domain.strategy.service.raffle;

import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 抽奖接口
 * @date 2025/2/15 12:19
 */
public interface IRaffleStrategy {

    //用户使用抽奖的入口
    RaffleAwardEntity preformRaffle(RaffleFactorEntity raffleFactorEntity);
}
