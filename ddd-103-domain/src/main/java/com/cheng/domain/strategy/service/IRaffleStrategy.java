package com.cheng.domain.strategy.service;

import com.cheng.domain.strategy.model.entity.RaffleAwardEntity;
import com.cheng.domain.strategy.model.entity.RaffleFactorEntity;
public interface IRaffleStrategy {

    //用户使用抽奖的入口
    RaffleAwardEntity preformRaffle(RaffleFactorEntity raffleFactorEntity);
}
