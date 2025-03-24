package org.example.domain.activity.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.aggregate.CreateOrderAggregate;
import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.model.vo.ActivitySkuStockKeyVo;
import org.example.domain.rebate.model.entity.skuRechargeEntity;
import org.example.domain.strategy.model.entity.ActivityAccountEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 程宇乐
 * @description 活动仓储接口
 *
 */


public interface IActivityRepository {

    //查询用户今日抽奖次数
    Integer queryRaffleActivityAccountDayPartakeCount(String userId, Long activityId);

    //查询活动详细信息
    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    //查询未使用的抽奖订单
    UserRaffleOrderEntity queryNoUseRaffleOrder(PartakeRaffleActivityEntity entity);

    //查询账户抽奖余额
    ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId);

    //查询月额度
    RaffleActivityAccountMonthEntity queryActivityAccountMouthByUserId(String userId, Long activityId, String mouth);
    //查询日额度
    RaffleActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day);

    void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate orderAggregate);

    ActivitySkuEntity queryActivitySku(Long sku);


    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    //根据 活动ID 查询出所有对应的 sku
    List<ActivitySkuEntity> queryActivitySkuByActivityId(Long activityId);

    boolean subtractionActivitySkuStock(Long sku, String cache, Date endDateTime);

    void cacheActivitySkuStockCount(String cache, Integer stockCount);

    void activitySkuStockSendQueue(ActivitySkuStockKeyVo build);

    CreateOrderAggregate buildOrderAggregate(skuRechargeEntity entity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

    void doSaveOrder(CreateOrderAggregate orderAggregate);

    ActivitySkuStockKeyVo takeQueueValue();

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    //查询用户抽奖总次数
    Integer queryUserDrawTotalCount(String userId, Long activityId);

    ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId);

    //清空redis
    boolean redisFlush();

    //创建用户
    void CreateUser(String userId, Long activityId);

    Integer queryUsedDrawTotalCount(String userId, Long activityId);
}
