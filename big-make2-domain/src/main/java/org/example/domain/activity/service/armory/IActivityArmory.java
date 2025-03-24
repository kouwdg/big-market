package org.example.domain.activity.service.armory;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 装配sku到redis
 * @date 2024/12/24 14:30
 */
public interface IActivityArmory {

    boolean assembleActivitySku(Long sku);

    //根据活动Id装配 sku
    boolean assembleActivitySkuByActivityId(Long ActivityId);

    //清空redis
    boolean redisFlush();

    //获得已抽奖的次数
    Integer getUsedRaffleCount(String userId, Long activityId);



}
