package com.cheng.infrastructure.persistent.dao;

import com.cheng.infrastructure.persistent.po.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/20 18:53
 */
@Mapper
public interface IRaffleActivitySkuDao {
    RaffleActivitySku queryActivitySku(@Param("sku") Long sku);

    void updateActivitySkuStock(@Param("sku")Long sku);

    void clearActivitySkuStock(@Param("sku")Long sku);
}
