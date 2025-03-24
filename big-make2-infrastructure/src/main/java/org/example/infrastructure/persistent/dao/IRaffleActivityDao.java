package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/18 12:32
 */
@Mapper
public interface IRaffleActivityDao {
     Long queryStrategyIdByActivityId(Long activityId);

     //根据活动ID查询活动具体数据
     RaffleActivity queryRaffleActivityByActivityId(Long activityId);
}
