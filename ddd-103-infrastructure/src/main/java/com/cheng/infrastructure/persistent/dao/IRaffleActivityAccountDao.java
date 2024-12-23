package com.cheng.infrastructure.persistent.dao;

import com.cheng.infrastructure.persistent.po.RaffleActivityAccount;
import org.apache.ibatis.annotations.Mapper;

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
}
