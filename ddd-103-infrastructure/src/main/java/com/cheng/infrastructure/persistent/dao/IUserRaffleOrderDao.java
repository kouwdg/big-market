package com.cheng.infrastructure.persistent.dao;

import com.cheng.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.cheng.domain.activity.model.entity.UserRaffleOrderEntity;
import com.cheng.infrastructure.persistent.po.UserRaffleOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/25 18:19
 */
@Mapper
public interface IUserRaffleOrderDao {
    void insert(UserRaffleOrder build);

    UserRaffleOrder queryNoUseRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);
}
