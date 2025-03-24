package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.UserRaffleOrder;
import org.example.infrastructure.persistent.po.UserAwardRecord;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/20 19:37
 */
@Mapper
public interface IUserAwardRecordDao {
    void insert(UserAwardRecord userAwardRecord);

    int updateUserRaffleOrderStateUsed(UserRaffleOrder userRaffleOrder);
}
