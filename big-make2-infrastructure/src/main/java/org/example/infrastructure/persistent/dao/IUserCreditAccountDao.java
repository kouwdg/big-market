package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.award.model.entity.UserCreditAwardEntity;
import org.example.infrastructure.persistent.po.UserCreditAccount;

import java.math.BigDecimal;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户积分表Dao
 * @date 2025/3/25 16:59
 */
@Mapper
public interface IUserCreditAccountDao {
    public UserCreditAccount queryByUserId(String userId);

    int insert(UserCreditAccount builder);

    void AddAmount(UserCreditAwardEntity creditAward);
}
