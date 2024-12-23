package com.cheng.domain.activity.service;

import com.cheng.domain.activity.model.entity.ActivityOrderEntity;
import com.cheng.domain.activity.model.entity.ActivityShopCartEntity;
import com.cheng.domain.activity.model.entity.skuRechargeEntity;
import org.springframework.stereotype.Service;

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
