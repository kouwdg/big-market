package org.example.domain.RechargeDraw.Model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/28 13:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleActivityOrderEntity {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long sku;

    //活动ID
    private Long activityId;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 订单状态（not_used、used、expire）
     */
    private String state;
    //充值数量
    private Integer skuCount;
}
