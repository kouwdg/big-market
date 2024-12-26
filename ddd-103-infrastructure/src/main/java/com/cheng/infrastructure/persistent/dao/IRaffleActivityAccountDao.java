package com.cheng.infrastructure.persistent.dao;

import com.cheng.domain.activity.model.entity.ActivityAccountEntity;
import com.cheng.infrastructure.persistent.po.RaffleActivityAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/23 15:43
 */
@Mapper
public interface IRaffleActivityAccountDao {
    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);

    void insert(RaffleActivityAccount raffleActivityAccount);

    //总额度自减
    int updateAccountSubtractionQuota(RaffleActivityAccount build);

    RaffleActivityAccount queryActivityAccountByUserId(@Param("userId") String userId,@Param("activityId") Long activityId);
}
