package com.cheng.domain.activity.model.entity;

import lombok.Data;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户抽奖单 入参对象
 * @date 2024/12/25 18:39
 */
@Data
public class PartakeRaffleActivityEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动Id
     */
    private Long activityId;
}
