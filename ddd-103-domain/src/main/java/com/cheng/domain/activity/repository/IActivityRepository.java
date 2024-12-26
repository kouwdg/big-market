package com.cheng.domain.activity.repository;


import com.cheng.domain.activity.model.aggregate.CreateOrderAggregate;
import com.cheng.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.cheng.domain.activity.model.entity.*;
import com.cheng.domain.activity.model.vo.ActivitySkuStockKeyVo;

import java.util.Date;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 活动仓储接口
 * @create 2024-03-16 10:31
 */
public interface IActivityRepository {

    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateOrderAggregate orderAggregate);

    void cacheActivitySkuStockCount(String cache, Integer stockCount);

    boolean subtractionActivitySkuStock(Long sku, String cache, Date endDateTime);

    void activitySkuStockSendQueue(ActivitySkuStockKeyVo build);

    ActivitySkuStockKeyVo takeQueueValue();

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

    void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate orderAggregate);

    UserRaffleOrderEntity queryNoUseRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);

    ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId);

    RaffleActivityAccountMonthEntity queryActivityAccountMouthByUserId(String userId, Long activityId, String mouth);

    RaffleActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day);
}
