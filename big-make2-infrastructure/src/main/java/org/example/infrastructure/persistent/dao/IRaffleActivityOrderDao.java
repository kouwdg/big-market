package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityOrder;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/21 13:20
 */
@Mapper
public interface IRaffleActivityOrderDao {
    void insert(RaffleActivityOrder raffleActivityOrder);
}
