package com.cheng.infrastructure.persistent.dao;

import com.cheng.infrastructure.persistent.po.raffleActivityCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/19 11:16
 */

@Mapper
public interface IRaffleActivityCountDao {
    raffleActivityCount queryRaffleActivityCountByActivityCountId(@Param("activityCountId") Long activityCountId);
}
