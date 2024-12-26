package com.cheng.infrastructure.persistent.dao;

import com.cheng.domain.activity.model.entity.RaffleActivityAccountMonthEntity;
import com.cheng.infrastructure.persistent.po.RaffleActivityAccountMonth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 程宇乐
 * @description 抽奖活动账户表-月次数
 * @create 2024-04-03 15:57
 */
@Mapper
public interface IRaffleActivityAccountMonthDao {
    void insert(RaffleActivityAccountMonthEntity activityAccountMonthEntity);

    int updateAccountSubtractionMonthQuota(RaffleActivityAccountMonthEntity activityAccountMonthEntity);

    RaffleActivityAccountMonth queryActivityAccountMouthByUserId(@Param("userId") String userId, @Param("activityId") Long activityId, @Param("month") String month);
}