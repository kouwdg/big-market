package org.example.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 程宇乐
 * @description 抽奖活动单 持久化对象
 * @create 2024-03-02 13:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleActivityOrder {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long sku;
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
     * 下单时间
     */
    private Date orderTime;
    /**
     * 订单状态（not_used、used、expire）
     */
    private String state;
    /**
     * 创建时间
     */
    private Date createTime;

    private Integer skuCount;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 业务防重Id
     */
    private String outBusinessNo;


}