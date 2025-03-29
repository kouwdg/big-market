package org.example.domain.credit.service;

import org.example.domain.credit.model.entity.TradeEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 充值 扣除 积分订单接口
 * @date 2025/3/27 16:47
 */
public interface ICreditAdjustService {

    //创建
    String creteOrder(TradeEntity entity);
}
