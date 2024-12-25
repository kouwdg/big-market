package com.cheng.infrastructure.persistent.repository;

import com.cheng.domain.activity.event.ActivitySkuStoreZeroMessageEvent;
import com.cheng.domain.activity.model.aggregate.CreateOrderAggregate;
import com.cheng.domain.activity.model.entity.ActivityCountEntity;
import com.cheng.domain.activity.model.entity.ActivityEntity;
import com.cheng.domain.activity.model.entity.ActivityOrderEntity;
import com.cheng.domain.activity.model.entity.ActivitySkuEntity;
import com.cheng.domain.activity.model.vo.ActivitySkuStockKeyVo;
import com.cheng.domain.activity.model.vo.ActivityStateVO;
import com.cheng.domain.activity.repository.IActivityRepository;
import com.cheng.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import com.cheng.infrastructure.event.eventPublisher;
import com.cheng.infrastructure.persistent.dao.*;
import com.cheng.infrastructure.persistent.po.*;
import com.cheng.infrastructure.persistent.redis.IRedisService;
import com.cheng.types.common.Constants;
import com.cheng.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 活动仓储的具体实现
 * @date 2024/12/23 13:03
 */
@Repository
@Slf4j
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IRedisService redisService;
    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;

    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;

    @Resource
    private eventPublisher eventPublisher;
    @Resource
    private ActivitySkuStoreZeroMessageEvent activitySkuStoreZeroMessageEvent;
    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku tem=raffleActivitySkuDao.queryActivitySku(sku);
        return ActivitySkuEntity.builder()
                        .sku(tem.getSku())
                        .activityId(tem.getActivityId())
                        .activityCountId(tem.getActivityCountId())
                        .stockCount(tem.getStockCount())
                        .stockCountSurplus(tem.getStockCountSurplus())
                        .build();
    }

    //查询抽奖活动
    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        //todo 优先从缓存中获取
        RaffleActivity tem=raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        return ActivityEntity.builder()
                    .activityId(tem.getActivityId())
                    .activityName(tem.getActivityName())
                    .activityDesc(tem.getActivityDesc())
                    .beginDateTime(tem.getBeginDateTime())
                    .endDateTime(tem.getEndDateTime())
                    .activityCountId(tem.getActivityCountId())
                    .strategyId(tem.getStrategyId())
                    .state(ActivityStateVO.valueOf(tem.getState()))
                    .build();
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        log.info("activityCountId:{}",activityCountId);
        //todo 优先从缓存中获取
        raffleActivityCount tem=raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
        log.info("tem:{}",tem);
        return ActivityCountEntity.builder()
              .activityCountId(tem.getActivityCountId())
              .totalCount(tem.getTotalCount())
              .dayCount(tem.getDayCount())
              .monthCount(tem.getMonthCount())
              .build();

    }

    @Override
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        // 订单对象
        ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        raffleActivityOrder.setUserId(activityOrderEntity.getUserId());
        raffleActivityOrder.setSku(activityOrderEntity.getSku());
        raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
        raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
        raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
        raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
        raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
        //todo 暂时 重置次数都是1
        raffleActivityOrder.setSkuCount(1);
        raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
        raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

        //账户对象
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(1);
        raffleActivityAccount.setTotalCountSurplus(1);
        raffleActivityAccount.setDayCount(1);
        raffleActivityAccount.setDayCountSurplus(1);
        raffleActivityAccount.setMonthCount(1);
        raffleActivityAccount.setMonthCountSurplus(1);

        try {
            //1 写入订单  //todo 暂时所有用户的订单流水都放入一个表中
            raffleActivityOrderDao.insert(raffleActivityOrder);
        }catch (Exception error){
            throw new AppException("200","写入订单记录，唯一索引冲突");
        }
        //2 更新账户
        int count = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
        if(count==0){
            //创建账户
            raffleActivityAccountDao.insert(raffleActivityAccount);
        }

    }

    /**
     *  保存sku库存到redis
     * @param cache redisKey
     * @param stockCount redisValue
     */
    @Override
    public void cacheActivitySkuStockCount(String cache, Integer stockCount) {
        //判断redis中是否存在
        if(redisService.isExists(cache))return;
        redisService.setAtomicLong(cache,stockCount);
    }

    /**
     *  扣减库存
     * @param sku
     * @param cache
     * @param endDateTime
     * @return
     */

    @Override
    public boolean subtractionActivitySkuStock(Long sku, String cache, Date endDateTime) {
        long decr = redisService.decr(cache);
        if (decr==0){
            //todo 库存消耗完毕 发送mq消息
            eventPublisher.publish(activitySkuStoreZeroMessageEvent.topic(),activitySkuStoreZeroMessageEvent.buildEventMessage(sku));
            return false;
        }else if(decr<0){
            //库存小于0 ，恢复为0
            redisService.setAtomicLong(cache,0);
            return false;
        }
        //加锁
        String lockKey=cache + Constants.UNDERLINE+decr;
        long expireMillis=endDateTime.getTime()-System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(1);
        Boolean lock= redisService.setNx(lockKey, expireMillis, TimeUnit.MINUTES);
        if(!lock){
            log.info("活动库存sku,加锁失败");
        }
        return lock;
    }

    /**
     * 延迟队列
     * @param build
     */
    @Override
    public void activitySkuStockSendQueue(ActivitySkuStockKeyVo build) {
        //延迟队列的key
        String cacheKey = Constants.redisKey.ACTIVITY_SKU_STOCK_COUNT_QUERY_KEY;
        //创建队列消息
        RBlockingQueue<ActivitySkuStockKeyVo> blockingQueue = redisService.getBlockingQueue(cacheKey);
        //将队列 转换成 延迟队列
        RDelayedQueue<ActivitySkuStockKeyVo> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        //填入消息
        delayedQueue.offer(build,3, TimeUnit.SECONDS);
    }

    @Override
    public ActivitySkuStockKeyVo takeQueueValue() {
        String cacheKey = Constants.redisKey.ACTIVITY_SKU_STOCK_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVo> activitySkuStockKeyVos = redisService.getBlockingQueue(cacheKey);
        return activitySkuStockKeyVos.poll();
    }

    @Override
    public void clearQueueValue() {
        String cacheKey = Constants.redisKey.ACTIVITY_SKU_STOCK_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVo> activitySkuStockKeyVos = redisService.getBlockingQueue(cacheKey);
        activitySkuStockKeyVos.clear();

    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        raffleActivitySkuDao.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        raffleActivitySkuDao.clearActivitySkuStock(sku);
    }
}
