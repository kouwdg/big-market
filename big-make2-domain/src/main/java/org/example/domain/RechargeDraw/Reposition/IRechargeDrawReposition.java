package org.example.domain.RechargeDraw.Reposition;

import org.example.domain.RechargeDraw.Model.aggregate.RechargeDrawAggregate;
import org.example.domain.RechargeDraw.Model.entity.RaffleActivityEntity;
import org.example.domain.RechargeDraw.Model.entity.RaffleActivitySkuEntity;

import java.math.BigDecimal;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 充值抽奖reposition
 * @date 2025/3/28 11:46
 */
public interface IRechargeDrawReposition {

    //判断用户的积分是否足够
    Boolean IsCreditEnough(String userId, Long sku, Integer count);

    RaffleActivitySkuEntity queryRaffleActivitySku(Long sku);

    RaffleActivityEntity queryRaffleActivityEntity(Long activity);

    BigDecimal queryProductAmount(Long sku);

    void saveRechageDrawAggregate(RechargeDrawAggregate build);
    void saveRechageDrawAggregateSignIn(RechargeDrawAggregate build);
    void updateRaffleActivityOrderToCompleted(String orderId);

    void addRaffleCount(String userId, Long activityId, Integer count);
}
