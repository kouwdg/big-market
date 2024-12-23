package com.cheng.domain.activity.service;

import com.cheng.domain.activity.model.entity.ActivityCountEntity;
import com.cheng.domain.activity.model.entity.ActivityEntity;
import com.cheng.domain.activity.model.entity.ActivitySkuEntity;
import com.cheng.domain.activity.repository.IActivityRepository;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 抽奖活动的支持类
 * @date 2024/12/23 13:55
 */
public class RaffleActivitySupport {
    protected IActivityRepository activityRepository;

    public RaffleActivitySupport(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    protected ActivitySkuEntity queryActivitySku(Long sku){
        return activityRepository.queryActivitySku(sku);
    }

    protected ActivityEntity queryRaffleActivityByActivityId(Long activityId){
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    protected ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId){
        return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }
}
