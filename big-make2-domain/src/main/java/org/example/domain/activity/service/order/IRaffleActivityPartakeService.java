package org.example.domain.activity.service.order;


import org.example.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 获得抽奖单的服务
 * @date 2024/12/25 18:35
 */
public interface IRaffleActivityPartakeService {

    /**
     * 创建抽奖单:
     * 1 扣除用户的库存
     * 2 产生抽奖单
     * 如果存在没有使用的抽奖单直接返回
     */
    UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity entity);

    UserRaffleOrderEntity createOrder(String userId,Long activityId);

}
