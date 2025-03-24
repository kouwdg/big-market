package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.example.infrastructure.persistent.po.UserRaffleOrder;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/20 18:05
 */
@Mapper
public interface IUserRaffleOrderDao {

    //查询用户未使用的抽奖订单
     UserRaffleOrder queryNoUseRaffleOrder(PartakeRaffleActivityEntity entity);

    void insert(UserRaffleOrder build);

    //更新 抽奖订单 为完成
    int updateUserRaffleOrderStateUsed(UserRaffleOrder userRaffleOrder);
}
