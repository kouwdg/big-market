package org.example.infrastructure.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.activity.event.ActivitySkuStoreZeroMessageEvent;
import org.example.domain.activity.model.aggregate.CreateOrderAggregate;
import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.model.vo.ActivitySkuStockKeyVo;
import org.example.domain.activity.model.vo.ActivityStateVO;
import org.example.domain.activity.model.vo.OrderStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.rebate.model.entity.skuRechargeEntity;
import org.example.domain.strategy.model.entity.ActivityAccountEntity;
import org.example.infrastructure.event.eventPublisher;
import org.example.infrastructure.persistent.dao.*;
import org.example.infrastructure.persistent.po.*;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.example.types.common.Constants;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/20 13:53
 */

@Service
@Slf4j
public class ActivityRepository implements IActivityRepository {

    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat MouthFormat = new SimpleDateFormat("yyyy-MM");
    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;

    @Resource
    private ActivitySkuStoreZeroMessageEvent activitySkuStoreZeroMessageEvent;
    @Resource
    private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;
    @Resource
    private IRedisService redisService;

    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;
    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;
    @Resource
    private eventPublisher eventPublisher;
    @Resource
    private IRaffleActivityAccountMonthDao raffleActivityAccountMonthDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;
    @Resource
    private IRaffleActivityDao raffleActivityDao;


    //查询用户抽奖次数
    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(String userId, Long activityId) {
        RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
        raffleActivityAccountDay.setActivityId(activityId);
        raffleActivityAccountDay.setUserId(userId);
        raffleActivityAccountDay.setDay(RaffleActivityAccountDay.currentDay());
        Integer dayPartakeCount = raffleActivityAccountDayDao.queryRaffleActivityAccountDayPartakeCount(raffleActivityAccountDay);
        return null == dayPartakeCount ? 0 : dayPartakeCount;
    }

    //查询抽奖活动
    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        String cacheKey = Constants.redisKey.RAFFLE_ACTIVITY_KET + activityId;
        ActivityEntity value = null;
        value = redisService.getValue(cacheKey);
        if (value != null) {
            return value;
        }
        RaffleActivity tem = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        value = ActivityEntity.builder()
                .activityId(tem.getActivityId())
                .activityName(tem.getActivityName())
                .activityDesc(tem.getActivityDesc())
                .beginDateTime(tem.getBeginDateTime())
                .endDateTime(tem.getEndDateTime())
                .activityCountId(tem.getActivityCountId())
                .strategyId(tem.getStrategyId())
                .state(ActivityStateVO.valueOf(tem.getState()))
                .build();
        redisService.setValue(cacheKey, value);
        return value;
    }

    //查询未使用的抽奖订单
    @Override
    public UserRaffleOrderEntity queryNoUseRaffleOrder(PartakeRaffleActivityEntity entity) {
        UserRaffleOrder userRaffleOrder = userRaffleOrderDao.queryNoUseRaffleOrder(entity);
        if (userRaffleOrder == null) return null;
        return UserRaffleOrderEntity.builder()
                .id(userRaffleOrder.getId())
                .userId(userRaffleOrder.getUserId())
                .activityId(userRaffleOrder.getActivityId())
                .activityName(userRaffleOrder.getActivityName())
                .strategyId(userRaffleOrder.getStrategyId())
                .orderId(userRaffleOrder.getOrderId())
                .orderTime(userRaffleOrder.getOrderTime())
                .orderState(userRaffleOrder.getOrderState())
                .build();
    }

    //查询账户抽奖余额
    @Override
    public ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId) {
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountDao.queryActivityAccountByUserId(userId, activityId);
        if (raffleActivityAccount == null) {
            return null;
        }
        return ActivityAccountEntity.builder()
                .userId(raffleActivityAccount.getUserId())
                .activityId(raffleActivityAccount.getActivityId())
                .totalCount(raffleActivityAccount.getTotalCount())
                .totalCountSurplus(raffleActivityAccount.getTotalCountSurplus())
                .dayCount(raffleActivityAccount.getDayCount())
                .dayCountSurplus(raffleActivityAccount.getDayCountSurplus())
                .monthCount(raffleActivityAccount.getMonthCount())
                .monthCountSurplus(raffleActivityAccount.getMonthCountSurplus())
                .build();
    }

    ///查询月额度
    @Override
    public RaffleActivityAccountMonthEntity queryActivityAccountMouthByUserId(String userId, Long activityId, String mouth) {
        RaffleActivityAccountMonth raffleActivityAccountMonth = raffleActivityAccountMonthDao.queryActivityAccountMouthByUserId(userId, activityId, mouth);
        if (raffleActivityAccountMonth == null) {
            return null;
        }
        return RaffleActivityAccountMonthEntity.builder()
                .id(raffleActivityAccountMonth.getId())
                .userId(raffleActivityAccountMonth.getUserId())
                .activityId(raffleActivityAccountMonth.getActivityId())
                .month(raffleActivityAccountMonth.getMonth())
                .monthCount(raffleActivityAccountMonth.getMonthCount())
                .monthCountSurplus(raffleActivityAccountMonth.getMonthCountSurplus())
                .createTime(raffleActivityAccountMonth.getCreateTime())
                .updateTime(raffleActivityAccountMonth.getUpdateTime())
                .build();
    }

    //查询日额度
    @Override
    public RaffleActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day) {
        RaffleActivityAccountDay raffleActivityAccountDay = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(userId, activityId, day);
        if (raffleActivityAccountDay == null) {
            return null;
        }
        return RaffleActivityAccountDayEntity.builder()
                .userId(raffleActivityAccountDay.getUserId())
                .activityId(raffleActivityAccountDay.getActivityId())
                .day(raffleActivityAccountDay.getDay())
                .dayCount(raffleActivityAccountDay.getDayCount())
                .dayCountSurplus(raffleActivityAccountDay.getDayCountSurplus())
                .createTime(raffleActivityAccountDay.getCreateTime())
                .updateTime(raffleActivityAccountDay.getUpdateTime())
                .build();
    }


    // 更新额度 写入抽奖订单
    @Override
    public void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate orderAggregate) {
        String userId = orderAggregate.getUserId();
        Long activityId = orderAggregate.getActivityId();
        RaffleActivityAccountDayEntity activityAccountDayEntity = orderAggregate.getActivityAccountDayEntity();
        RaffleActivityAccountMonthEntity activityAccountMonthEntity = orderAggregate.getActivityAccountMonthEntity();
        UserRaffleOrderEntity userRaffleOrder = orderAggregate.getUserRaffleOrder();

        //1 更新总额度
        int totalCount = raffleActivityAccountDao.updateAccountSubtractionQuota(
                RaffleActivityAccount.builder()
                        .userId(userId)
                        .activityId(activityId)
                        .build());
        if (totalCount != 1) {
            log.info("更新失败，额度不足");
            throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
        }

        //更新月额度

        //创建月额度
        if (!orderAggregate.isExistAccountMouth()) {
            raffleActivityAccountMonthDao.insert(activityAccountMonthEntity);
        }
        //更新月额度
        int updateMonth = raffleActivityAccountMonthDao.updateAccountSubtractionMonthQuota(activityAccountMonthEntity);
        if (updateMonth != 1) {
            log.info("更新失败，月额度不足");
            throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());

        }

        //更新日额度
        //创建日额度
        if (!orderAggregate.isExistAccountDay()) {
            raffleActivityAccountDayDao.insert(activityAccountDayEntity);
        }
        //更新日额度
        int updateDay = raffleActivityAccountDayDao.updateAccountSubtractionDayQuota(activityAccountDayEntity);
        if (updateDay != 1) {
            log.info("更新失败，日额度不足");
            throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getInfo());
        }

        //写入参与活动订单
        userRaffleOrderDao.insert(UserRaffleOrder.builder()
                .userId(userId)
                .activityId(activityId)
                .activityName(userRaffleOrder.getActivityName())
                .strategyId(userRaffleOrder.getStrategyId())
                .orderId(userRaffleOrder.getOrderId())
                .orderTime(userRaffleOrder.getOrderTime())
                .orderState(userRaffleOrder.getOrderState())
                .build());
    }

    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku tem = raffleActivitySkuDao.queryActivitySku(sku);
        return ActivitySkuEntity.builder()
                .sku(tem.getSku())
                .activityId(tem.getActivityId())
                .activityCountId(tem.getActivityCountId())
                .stockCount(tem.getStockCount())
                .stockCountSurplus(tem.getStockCountSurplus())
                .build();
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        String cacheKey = Constants.redisKey.RAFFLE_ACTIVITY_COUNT_KET + activityCountId;
        ActivityCountEntity value = null;
        value = redisService.getValue(cacheKey);
        if (value != null) {
            return value;
        }
        RaffleActivityCount tem = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
        value = ActivityCountEntity.builder()
                .activityCountId(tem.getActivityCountId())
                .totalCount(tem.getTotalCount())
                .dayCount(tem.getDayCount())
                .monthCount(tem.getMonthCount())
                .build();
        redisService.setValue(cacheKey, value);
        return value;
    }


    //根据 活动ID 查询出所有对应的 sku
    @Override
    public List<ActivitySkuEntity> queryActivitySkuByActivityId(Long activityId) {
        List<ActivitySkuEntity> resultList = new ArrayList<>();
        List<RaffleActivitySku> temList = raffleActivitySkuDao.queryActivitySkuByActivityId(activityId);
        for (RaffleActivitySku tem : temList) {
            ActivitySkuEntity result = ActivitySkuEntity.builder()
                    .sku(tem.getSku())
                    .activityId(tem.getActivityId())
                    .activityCountId(tem.getActivityCountId())
                    .stockCount(tem.getStockCount())
                    .stockCountSurplus(tem.getStockCountSurplus())
                    .build();
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, String cache, Date endDateTime) {
        long decr = redisService.decr(cache);
        if (decr == 0) {
            //todo 库存消耗完毕 发送mq消息
            eventPublisher.publish(activitySkuStoreZeroMessageEvent.topic(), activitySkuStoreZeroMessageEvent.buildEventMessage(sku));
            return false;
        } else if (decr < 0) {
            //库存小于0 ，恢复为0
            redisService.setAtomicLong(cache, 0);
            return false;
        }
        //加锁
        String lockKey = cache + Constants.UNDERLINE + decr;
        long expireMillis = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, expireMillis, TimeUnit.MINUTES);
        if (!lock) {
            log.info("活动库存sku,加锁失败");
        }
        return lock;
    }

    @Override
    public void cacheActivitySkuStockCount(String cache, Integer stockCount) {
        //判断redis中是否存在
        if (redisService.isExists(cache)) return;
        redisService.setAtomicLong(cache, stockCount);
    }

    @Override
    public void activitySkuStockSendQueue(ActivitySkuStockKeyVo build) {
        //延迟队列的key
        String cacheKey = Constants.redisKey.ACTIVITY_SKU_STOCK_COUNT_QUERY_KEY;
        //创建队列消息
        RBlockingQueue<ActivitySkuStockKeyVo> blockingQueue = redisService.getBlockingQueue(cacheKey);
        //将队列 转换成 延迟队列
        RDelayedQueue<ActivitySkuStockKeyVo> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        //填入消息
        delayedQueue.offer(build, 3, TimeUnit.SECONDS);
    }

    @Override
    public CreateOrderAggregate buildOrderAggregate(skuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        //订单实体对象
        ActivityOrderEntity activityOrderEntity = new ActivityOrderEntity();
        activityOrderEntity.setUserId(skuRechargeEntity.getUserId());
        activityOrderEntity.setSku(skuRechargeEntity.getSku());
        activityOrderEntity.setActivityId(activityEntity.getActivityId());
        activityOrderEntity.setActivityName(activityEntity.getActivityName());
        activityOrderEntity.setStrategyId(activityEntity.getStrategyId());
        // 公司里一般会有专门的雪花算法UUID服务，我们这里直接生成个12位就可以了。
        activityOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrderEntity.setOrderTime(new Date());
        activityOrderEntity.setTotalCount(activityCountEntity.getTotalCount());
        activityOrderEntity.setDayCount(activityCountEntity.getDayCount());
        activityOrderEntity.setMonthCount(activityCountEntity.getMonthCount());
        activityOrderEntity.setState(OrderStateVO.completed);
        activityOrderEntity.setOutBusinessNo(skuRechargeEntity.getOutBusinessNo());

        // 构建聚合对象
        return CreateOrderAggregate.builder()
                .userId(skuRechargeEntity.getUserId())
                .activityId(activitySkuEntity.getActivityId())
                .totalCount(activityCountEntity.getTotalCount())
                .dayCount(activityCountEntity.getDayCount())
                .monthCount(activityCountEntity.getMonthCount())
                .count(Integer.parseInt(skuRechargeEntity.getSkuConfig()))
                .activityOrderEntity(activityOrderEntity)
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

        raffleActivityOrder.setSkuCount(1);
        raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
        raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

        //账户对象
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(100);
        raffleActivityAccount.setTotalCountSurplus(100);
        raffleActivityAccount.setDayCount(100);
        raffleActivityAccount.setDayCountSurplus(100);
        raffleActivityAccount.setMonthCount(100);
        raffleActivityAccount.setMonthCountSurplus(100);

        try {
            //1 插入订单 (添加抽奖次数也要留下订单)
            raffleActivityOrderDao.insert(raffleActivityOrder);
        } catch (Exception error) {
            throw new AppException("200", "写入订单记录，唯一索引冲突");
        }
        //2 更新账户
        int count = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
        if (count == 0) {
            //如果更新失败就证明没有该用户,创建用户
            raffleActivityAccountDao.insert(raffleActivityAccount);
        }

        //3 更新日账户 与 月账户
        RaffleActivityAccountDay day = new RaffleActivityAccountDay();
        day.setUserId(createOrderAggregate.getUserId());
        day.setActivityId(createOrderAggregate.getActivityId());
        day.setDay(dayFormat.format(new Date()));

        int dayCount = raffleActivityAccountDayDao.AddSurplusCount(day);
        if (dayCount == 0) {
            //如果更新失败就证明没有该用户,创建用户
            RaffleActivityAccount tem = raffleActivityAccountDao.queryActivityAccountByUserId(createOrderAggregate.getUserId(), createOrderAggregate.getActivityId());
            day.setDayCount(tem.getDayCount());
            day.setDayCountSurplus(tem.getDayCountSurplus());
            raffleActivityAccountDayDao.insertPo(day);
        }

        //月账户
        RaffleActivityAccountMonth mouth = new RaffleActivityAccountMonth();
        mouth.setUserId(createOrderAggregate.getUserId());
        mouth.setActivityId(createOrderAggregate.getActivityId());
        mouth.setMonth(MouthFormat.format(new Date()));
        //1 查询是否存在
        int mouthCount = raffleActivityAccountMonthDao.AddSurplusCount(mouth);
        if (mouthCount == 0) {
            //如果更新失败就证明没有该用户,创建用户
            RaffleActivityAccount tem = raffleActivityAccountDao.queryActivityAccountByUserId(createOrderAggregate.getUserId(), createOrderAggregate.getActivityId());
            mouth.setMonthCount(tem.getMonthCount());
            mouth.setMonthCountSurplus(tem.getMonthCountSurplus());
            raffleActivityAccountMonthDao.insertPo(mouth);
            log.info("成功");
        }
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

    //查询用户已抽奖的次数
    @Override
    public Integer queryUserDrawTotalCount(String userId, Long activityId) {
        //1 查询用户抽奖次数信息
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountDao.queryActivityAccountByUserId(userId, activityId);
        if (raffleActivityAccount == null) {
            //抛出异常 用户或活动不存在
            throw new AppException(ResponseCode.USER_OR_ACTIVITY_NOT_EXIST.getCode(), ResponseCode.USER_OR_ACTIVITY_NOT_EXIST.getInfo());
        }
        return raffleActivityAccount.getTotalCount() - raffleActivityAccount.getTotalCountSurplus();
    }

    @Override
    public ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId) {
        RaffleActivityAccount activityAccount = new RaffleActivityAccount();
        activityAccount.setActivityId(activityId);
        activityAccount.setUserId(userId);
        //查询总账户
        RaffleActivityAccount tem = raffleActivityAccountDao.queryActivityAccountEntity(activityAccount);
        if (tem == null) {
            return ActivityAccountEntity.builder()
                    .userId(tem.getUserId())
                    .activityId(tem.getActivityId())
                    .totalCount(0)
                    .totalCountSurplus(0)
                    .dayCount(0)
                    .dayCountSurplus(0)
                    .monthCount(0)
                    .monthCountSurplus(0)
                    .build();
        }

        return ActivityAccountEntity.builder()
                .userId(tem.getUserId())
                .activityId(tem.getActivityId())
                .totalCount(tem.getTotalCount())
                .totalCountSurplus(tem.getTotalCountSurplus())
                .dayCount(tem.getDayCount())
                .dayCountSurplus(tem.getDayCountSurplus())
                .monthCount(tem.getMonthCount())
                .monthCountSurplus(tem.getMonthCountSurplus())
                .build();
    }

    @Override
    public boolean redisFlush() {
        redisService.flush();
        return true;
    }

    @Override
    public void CreateUser(String userId, Long activityId) {
        RaffleActivityAccount build = RaffleActivityAccount.builder()
                .activityId(activityId)
                .userId(userId)
                .totalCount(0)
                .totalCountSurplus(0)
                .monthCount(0)
                .monthCountSurplus(0)
                .dayCount(0)
                .dayCountSurplus(0)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        raffleActivityAccountDao.insert(build);
    }

    @Override
    public Integer queryUsedDrawTotalCount(String userId, Long activityId) {
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountDao.queryActivityAccountByUserId(userId, activityId);
        if (raffleActivityAccount==null){
            System.out.println("用户不存在");
            return 0;
        }
        return raffleActivityAccount.getTotalCount()-raffleActivityAccount.getTotalCountSurplus();
    }
}
