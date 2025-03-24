package org.example.domain.activity.service.quota;


import org.example.domain.strategy.model.entity.ActivityAccountEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 与用户抽奖额度相关的函数
 * @date 2024/12/28 17:27
 */
public interface IRaffleActivityAccountQuota {

    //查询用户今日的抽奖次数
    Integer queryRaffleActivityAccountDayPartakeCount(String userId,Long activityId);

    Long strategyIdToActivityId(Long activityId);
    //查询用户的额度
    ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId);

    //查询用户 已经抽奖的次数
    Integer queryUserDrawTotalCount(String userId, Long activityId);

    //创建用户
    void createUser(String userId,Long activityId);
}
