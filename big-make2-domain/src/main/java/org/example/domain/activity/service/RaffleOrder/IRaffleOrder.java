package org.example.domain.activity.service.RaffleOrder;


import org.example.domain.activity.model.entity.ActivityOrderEntity;
import org.example.domain.activity.model.entity.ActivityShopCartEntity;
import org.example.domain.rebate.model.entity.skuRechargeEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 抽奖活动订单接口
 * @date 2024/12/20 19:02
 */

public interface IRaffleOrder {
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity entity);


//    创建sku充值订单
    String createSkuRechargeOrder(skuRechargeEntity entity);
}
