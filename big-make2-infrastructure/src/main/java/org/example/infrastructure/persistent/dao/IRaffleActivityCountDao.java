package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityAccount;
import org.example.infrastructure.persistent.po.RaffleActivityCount;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/20 18:15
 */
@Mapper
public interface IRaffleActivityCountDao {
    //查询抽奖剩余次数
    RaffleActivityAccount queryActivityAccountByUserId(String userId, Long activityId);

    //
    RaffleActivityCount queryRaffleActivityCountByActivityCountId(Long activityCountId);
}
