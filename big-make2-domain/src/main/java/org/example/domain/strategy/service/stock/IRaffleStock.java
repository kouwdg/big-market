package org.example.domain.strategy.service.stock;


import org.example.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import org.springframework.stereotype.Service;

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
