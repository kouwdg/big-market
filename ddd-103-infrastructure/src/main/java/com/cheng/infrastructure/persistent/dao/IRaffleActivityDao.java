package com.cheng.infrastructure.persistent.dao;

import com.cheng.infrastructure.persistent.po.RaffleActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/19 11:09
 */

@Mapper
public interface IRaffleActivityDao {
    RaffleActivity queryRaffleActivity(@Param("ActivityId") Long ActivityId);

    RaffleActivity queryRaffleActivityByActivityId(@Param("ActivityId")Long activityId);
}
