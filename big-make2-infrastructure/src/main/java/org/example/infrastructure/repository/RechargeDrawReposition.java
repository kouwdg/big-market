package org.example.infrastructure.repository;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.RechargeDraw.Model.aggregate.RechargeDrawAggregate;
import org.example.domain.RechargeDraw.Model.entity.*;
import org.example.domain.RechargeDraw.Reposition.IRechargeDrawReposition;
import org.example.infrastructure.event.eventPublisher;
import org.example.infrastructure.persistent.dao.*;
import org.example.infrastructure.persistent.po.*;
import org.example.types.common.Constants;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 充值抽奖次数的Reposition
 * @date 2025/3/28 11:47
 */
@Repository
@Slf4j
public class RechargeDrawReposition implements IRechargeDrawReposition {

    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat MouthFormat = new SimpleDateFormat("yyyy-MM");
    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private IRaffleActivityDao raffleActivityDao;
    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
    @Resource
    private eventPublisher eventPublisher;

    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Resource
    private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;

    @Resource
    private IRaffleActivityAccountMonthDao raffleActivityAccountMonthDao;
    @Resource
    private ITaskDao taskDao;


    //判断用户的积分是否足够
    @Override
    public Boolean IsCreditEnough(String userId, Long sku, Integer count) {
        //1 查询sku中的积分价格
        String redisKey = "sku_productAmount:" + sku;
        RBucket<BigDecimal> skuProductAmount = redissonClient.getBucket(redisKey);
        BigDecimal productAmount = skuProductAmount.get();
        if (productAmount == null) {
            RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);
            productAmount = raffleActivitySku.getProductAmount();
            if (raffleActivitySku == null) {
                throw new IllegalArgumentException("无效的SKU: " + sku);
            }
            System.out.println(raffleActivitySku);
            skuProductAmount.set(raffleActivitySku.getProductAmount());
        }

        //2 计算总积分
        BigDecimal UseCredit = productAmount.multiply(new BigDecimal(count));

        //3 获得用户积分
        BigDecimal totalCredit = userCreditAccountDao.getTotalCredit(userId);

        return totalCredit.compareTo(UseCredit) >= 0;
    }

    @Override
    public RaffleActivitySkuEntity queryRaffleActivitySku(Long sku) {
        RaffleActivitySku a = raffleActivitySkuDao.queryActivitySku(sku);
        if (a == null) {
            return null;
        }
        return RaffleActivitySkuEntity.builder()
                .sku(a.getSku())
                .activityId(a.getActivityId())
                .activityCountId(a.getActivityCountId())
                .stockCount(a.getStockCount())
                .stockCountSurplus(a.getStockCountSurplus())
                .productAmount(a.getProductAmount())
                .build();
    }

    @Override
    public RaffleActivityEntity queryRaffleActivityEntity(Long activity) {
        RaffleActivity tem = raffleActivityDao.queryRaffleActivityByActivityId(activity);
        if (tem == null) {
            return null;
        }
        return RaffleActivityEntity.builder()
                .activityId(tem.getActivityId())
                .activityName(tem.getActivityName())
                .activityDesc(tem.getActivityDesc())
                .beginDateTime(tem.getBeginDateTime())
                .endDateTime(tem.getEndDateTime())
                .activityCountId(tem.getActivityCountId())
                .strategyId(tem.getStrategyId())
                .state(tem.getState())
                .build();
    }

    @Override
    public BigDecimal queryProductAmount(Long sku) {
        String redisKey = "sku_productAmount:" + sku;
        RBucket<BigDecimal> skuProductAmount = redissonClient.getBucket(redisKey);
        if (skuProductAmount == null) {
            RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);
            skuProductAmount.set(raffleActivitySku.getProductAmount());
        }
        return skuProductAmount.get();
    }


    @Transactional
    @Override
    public void saveRechageDrawAggregate(RechargeDrawAggregate build) {
        //1 添加订单
        RaffleActivityOrderEntity raffleActivityOrderEntity = build.getRaffleActivityOrderEntity();

        RaffleActivityOrder raffleActivityOrder = RaffleActivityOrder.builder()
                .userId(raffleActivityOrderEntity.getUserId())
                .sku(raffleActivityOrderEntity.getSku())
                .activityId(raffleActivityOrderEntity.getActivityId())
                .activityName(raffleActivityOrderEntity.getActivityName())
                .strategyId(raffleActivityOrderEntity.getStrategyId())
                .orderId(raffleActivityOrderEntity.getOrderId())
                .state(raffleActivityOrderEntity.getState())
                .skuCount(raffleActivityOrderEntity.getSkuCount())
                .orderTime(new Date())
                .outBusinessNo(raffleActivityOrderEntity.getUserId() + Constants.UNDERLINE + "sku: " + RandomStringUtils.randomNumeric(12))
                .build();
        raffleActivityOrderDao.insert(raffleActivityOrder);

        //2 扣减积分
        UserCreditAwardEntity entity = build.getUserCreditAwardEntity();
        userCreditAccountDao.ReduceAmount(entity.getUserId(), entity.getCreditAmount());

        TaskEntity taskEntity = build.getTaskEntity();
        Task task = Task.builder()
                .userId(taskEntity.getUserId())
                .topic(taskEntity.getTopic())
                .message(JSON.toJSONString(taskEntity.getMessage()))
                .messageId(taskEntity.getMessageId())
                .state(taskEntity.getState().getCode())
                .build();
        //3 添加任务订单
        taskDao.insert(task);

        //4发送MQ消息
        try {
            eventPublisher.publish(task.getTopic(), task.getMessage());
            taskDao.updateTaskSendMessageCompleted(task);
        } catch (Exception e) {
            log.error("写入返利记录，发送MQ消息失败 userId{},task.getTopic()", task.getUserId(), task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }

    }

    @Transactional
    @Override
    public void saveRechageDrawAggregateSignIn(RechargeDrawAggregate build) {
        //1 添加订单
        RaffleActivityOrderEntity raffleActivityOrderEntity = build.getRaffleActivityOrderEntity();

        RaffleActivityOrder raffleActivityOrder = RaffleActivityOrder.builder()
                .userId(raffleActivityOrderEntity.getUserId())
                .sku(raffleActivityOrderEntity.getSku())
                .activityId(raffleActivityOrderEntity.getActivityId())
                .activityName(raffleActivityOrderEntity.getActivityName())
                .strategyId(raffleActivityOrderEntity.getStrategyId())
                .orderId(raffleActivityOrderEntity.getOrderId())
                .state(raffleActivityOrderEntity.getState())
                .skuCount(raffleActivityOrderEntity.getSkuCount())
                .orderTime(new Date())
                .outBusinessNo(raffleActivityOrderEntity.getUserId() + Constants.UNDERLINE + "sku" + LocalDate.now())
                .build();
        raffleActivityOrderDao.insert(raffleActivityOrder);

        TaskEntity taskEntity = build.getTaskEntity();
        Task task = Task.builder()
                .userId(taskEntity.getUserId())
                .topic(taskEntity.getTopic())
                .message(JSON.toJSONString(taskEntity.getMessage()))
                .messageId(taskEntity.getMessageId())
                .state(taskEntity.getState().getCode())
                .build();
        //3 添加任务订单
        taskDao.insert(task);

        //4发送MQ消息
        try {
            eventPublisher.publish(task.getTopic(), task.getMessage());
            taskDao.updateTaskSendMessageCompleted(task);
        } catch (Exception e) {
            log.error("写入返利记录，发送MQ消息失败 userId{},task.getTopic()", task.getUserId(), task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }
    }

    @Override
    public void updateRaffleActivityOrderToCompleted(String orderId) {
        raffleActivityOrderDao.updateRaffleActivityOrderToCompleted(orderId);
    }

    @Override
    public void addRaffleCount(String userId, Long activityId, Integer count) {

        //1 更新总账户
        int tem = raffleActivityAccountDao.AddCount(userId, activityId, count);
        if (count == 0) {
            //账户对象
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            raffleActivityAccount.setUserId(userId);
            raffleActivityAccount.setActivityId(activityId);
            raffleActivityAccount.setTotalCount(0);
            raffleActivityAccount.setTotalCountSurplus(0);
            raffleActivityAccount.setDayCount(0);
            raffleActivityAccount.setDayCountSurplus(0);
            raffleActivityAccount.setMonthCount(0);
            raffleActivityAccount.setMonthCountSurplus(0);
            //如果更新失败就证明没有该用户,创建用户
            raffleActivityAccountDao.insert(raffleActivityAccount);
            raffleActivityAccountDao.AddCount(userId, activityId, count);
        }

        //2 更新日账户
        RaffleActivityAccountDay day = new RaffleActivityAccountDay();
        day.setUserId(userId);
        day.setActivityId(activityId);
        day.setDay(dayFormat.format(new Date()));

        int dayCount = raffleActivityAccountDayDao.AddSurplusCount(day);
        if (dayCount == 0) {
            //如果更新失败就证明没有该用户,创建用户
            RaffleActivityAccount tem1 = raffleActivityAccountDao.queryActivityAccountByUserId(userId, activityId);
            day.setDayCount(tem1.getDayCount());
            day.setDayCountSurplus(tem1.getDayCountSurplus());
            raffleActivityAccountDayDao.insertPo(day);
        }

        //2 更新月账户
        RaffleActivityAccountMonth mouth = new RaffleActivityAccountMonth();
        mouth.setUserId(userId);
        mouth.setActivityId(activityId);
        mouth.setMonth(MouthFormat.format(new Date()));
        //1 查询是否存在
        int mouthCount = raffleActivityAccountMonthDao.AddSurplusCount(mouth);
        if (mouthCount == 0) {
            //如果更新失败就证明没有该用户,创建用户
            RaffleActivityAccount tem1 = raffleActivityAccountDao.queryActivityAccountByUserId(userId,activityId);
            mouth.setMonthCount(tem1.getMonthCount());
            mouth.setMonthCountSurplus(tem1.getMonthCountSurplus());
            raffleActivityAccountMonthDao.insertPo(mouth);
            log.info("成功");
        }
    }
}
