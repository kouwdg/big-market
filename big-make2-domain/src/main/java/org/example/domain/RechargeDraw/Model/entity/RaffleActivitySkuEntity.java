package org.example.domain.RechargeDraw.Model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/28 13:26
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleActivitySkuEntity {
    /**
     * 商品sku
     */
    private Long sku;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 活动个人参与次数ID
     */
    private Long activityCountId;
    /**
     * 库存总量
     */
    private Integer stockCount;
    /**
     * 剩余库存
     */
    private Integer stockCountSurplus;

    //积分价格
    private BigDecimal productAmount;
}
