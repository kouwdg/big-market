package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.domain.activity.model.entity.RaffleActivityAccountMonthEntity;
import org.example.infrastructure.persistent.po.RaffleActivityAccountMonth;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/20 18:26
 */
@Mapper
public interface IRaffleActivityAccountMonthDao {
    //查询月额度
    RaffleActivityAccountMonth queryActivityAccountMouthByUserId(@Param("userId") String userId, @Param("activityId") Long activityId, @Param("month") String month);

    //创建月额度订单
    void insert(RaffleActivityAccountMonthEntity activityAccountMonthEntity);


    //更新月额度
    int updateAccountSubtractionMonthQuota(RaffleActivityAccountMonthEntity activityAccountMonthEntity);

    int AddSurplusCount(RaffleActivityAccountMonth mouth);

    void insertPo(RaffleActivityAccountMonth mouth);
}
