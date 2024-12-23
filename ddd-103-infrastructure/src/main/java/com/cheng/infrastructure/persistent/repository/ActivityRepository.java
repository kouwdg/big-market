package com.cheng.infrastructure.persistent.repository;

import com.cheng.domain.activity.model.aggregate.CreateOrderAggregate;
import com.cheng.domain.activity.model.entity.ActivityCountEntity;
import com.cheng.domain.activity.model.entity.ActivityEntity;
import com.cheng.domain.activity.model.entity.ActivityOrderEntity;
import com.cheng.domain.activity.model.entity.ActivitySkuEntity;
import com.cheng.domain.activity.model.vo.ActivityStateVO;
import com.cheng.domain.activity.repository.IActivityRepository;
import com.cheng.infrastructure.persistent.dao.*;
import com.cheng.infrastructure.persistent.po.*;
import com.cheng.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;

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
    private IRaffleActivitySkuDao raffleActivitySkuDao;

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;

    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;
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
            log.info("{}",1);
        }catch (Exception error){
            throw new AppException("200","写入订单记录，唯一索引冲突");
        }
        //2 更新账户
        int count = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
        log.info("{}",2);
        if(count==0){
            //创建账户
            raffleActivityAccountDao.insert(raffleActivityAccount);
        }

    }
}
