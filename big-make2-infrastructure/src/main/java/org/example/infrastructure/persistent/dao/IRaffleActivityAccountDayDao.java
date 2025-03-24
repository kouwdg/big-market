package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.domain.activity.model.entity.RaffleActivityAccountDayEntity;
import org.example.infrastructure.persistent.po.RaffleActivityAccountDay;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/20 13:58
 */
@Mapper
public interface IRaffleActivityAccountDayDao {

    //查询用户当日的抽奖次数
    Integer queryRaffleActivityAccountDayPartakeCount(RaffleActivityAccountDay raffleActivityAccountDay);

    //查询日额度详细信息
    RaffleActivityAccountDay queryActivityAccountDayByUserId(@Param("userId") String userId, @Param("activityId") Long activityId, @Param("day") String day);

    void insert(RaffleActivityAccountDayEntity activityAccountDayEntity);

    int updateAccountSubtractionDayQuota(RaffleActivityAccountDayEntity activityAccountDayEntity);

    int AddSurplusCount(RaffleActivityAccountDay day);

    void insertPo(RaffleActivityAccountDay day);
}
