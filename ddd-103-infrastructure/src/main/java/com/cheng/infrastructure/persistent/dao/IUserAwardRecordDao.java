package com.cheng.infrastructure.persistent.dao;

import com.cheng.infrastructure.persistent.po.UserAwardRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/25 18:18
 */
@Mapper
public interface IUserAwardRecordDao {
    void insert(UserAwardRecord userAwardRecord);
}
