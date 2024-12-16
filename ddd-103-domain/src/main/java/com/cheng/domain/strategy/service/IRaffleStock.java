package com.cheng.domain.strategy.service;

import com.cheng.domain.strategy.model.vo.StrategyAwardStockKeyVo;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 抽奖库存相关服务，消耗库存队列
 * @date 2024/12/13 14:30
 */
public interface IRaffleStock {

    //获取 奖品库存的消耗队列
    StrategyAwardStockKeyVo takeQueryValue()throws InterruptedException;

    //更新奖品
    void updateStrategyAwardStock(Long strategy,Integer award);
}
