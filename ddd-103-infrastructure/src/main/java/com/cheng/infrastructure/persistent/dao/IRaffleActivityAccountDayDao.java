package com.cheng.infrastructure.persistent.dao;

import com.cheng.domain.activity.model.entity.RaffleActivityAccountDayEntity;
import com.cheng.infrastructure.persistent.po.RaffleActivityAccountDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 程宇乐
 * @description 抽奖活动账户表-日次数
 * @create 2024-04-03 15:56
 */
@Mapper
public interface IRaffleActivityAccountDayDao {
    void insert(RaffleActivityAccountDayEntity activityAccountDayEntity);

    int updateAccountSubtractionDayQuota(RaffleActivityAccountDayEntity activityAccountDayEntity);

    RaffleActivityAccountDay queryActivityAccountDayByUserId(@Param("userId") String userId, @Param("activityId") Long activityId, @Param("day") String day);
}