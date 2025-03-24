package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.RaffleActivityAccount;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/20 18:20
 */
@Mapper
public interface IRaffleActivityAccountDao {
    //查询用户额度信息
    RaffleActivityAccount queryActivityAccountByUserId(@Param("userId") String userId,@Param("activityId") Long activityId);
    //总额度自减
    int updateAccountSubtractionQuota(RaffleActivityAccount build);

    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);

    void insert(RaffleActivityAccount raffleActivityAccount);

    RaffleActivityAccount queryActivityAccountEntity(RaffleActivityAccount activityAccount);
}
