package org.example.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户抽奖订单
 * @date 2024/12/25 18:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRaffleOrderEntity {

    private String id;

    private String userId;

    //活动Id
    private Long activityId;

    //活动名字
    private String activityName;

    private Long strategyId;

    private String orderId;

    private Date orderTime;

    private String orderState;


}
