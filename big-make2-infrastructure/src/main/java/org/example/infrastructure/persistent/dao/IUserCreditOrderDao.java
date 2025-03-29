package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.UserCreditOrder;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/27 16:39
 */
@Mapper
public interface IUserCreditOrderDao {
    void insert(UserCreditOrder userCreditOrder);
}
