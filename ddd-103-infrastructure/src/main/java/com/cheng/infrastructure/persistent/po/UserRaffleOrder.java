package com.cheng.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户抽奖订单表
 * @create 2024-04-03 15:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRaffleOrder {


    private String id;

    private String userId;

    private Long activityId;

    private String activityName;

    private Long strategyId;

    private String orderId;

    private Date orderTime;

    private String orderState;

    private Date createTime;

    private Date updateTime;

}
