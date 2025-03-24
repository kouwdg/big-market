package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.RaffleActivitySku;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/21 11:33
 */
@Mapper
public interface IRaffleActivitySkuDao {

    RaffleActivitySku queryActivitySku(Long sku);

    List<RaffleActivitySku> queryActivitySkuByActivityId(@Param("activityId") Long activityId);

    void updateActivitySkuStock(@Param("sku") Long sku);
}
