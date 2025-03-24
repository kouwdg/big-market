package org.example.domain.activity.service.quota;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.strategy.model.entity.ActivityAccountEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/18 19:48
 */
@Service

public class RaffleActivityAccountQuota implements IRaffleActivityAccountQuota{

    @Resource
    private IActivityRepository activityRepository;

    /**
     * 查询用户今日抽奖次数
     * @param userId
     * @param activityId
     * @return
     */
    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(String userId, Long activityId) {
        return activityRepository.queryRaffleActivityAccountDayPartakeCount(userId,activityId);
    }

    @Override
    public Long strategyIdToActivityId(Long activityId) {
        return null;
    }

    @Override
    public ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId) {
        return activityRepository.queryActivityAccountEntity(activityId,userId);
    }

    @Override
    public Integer queryUserDrawTotalCount(String userId, Long activityId) {
        return activityRepository.queryUserDrawTotalCount(userId,activityId);
    }


    //创建用户
    @Override
    public void createUser(String userId,Long activityId) {
        synchronized (this){
            //1 查询用户是否存在
            ActivityAccountEntity entity = activityRepository.queryActivityAccountByUserId(userId, activityId);
            if (entity==null){
                //创建用户
                activityRepository.CreateUser(userId,activityId);
            }
        }

    }
}
