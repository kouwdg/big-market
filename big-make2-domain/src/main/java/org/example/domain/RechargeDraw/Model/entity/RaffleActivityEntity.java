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
 * @date 2025/3/28 13:42
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleActivityEntity {

    // 活动ID
    private Long activityId;
    // 活动名称
    private String activityName;
    // 活动描述
    private String activityDesc;
    // 开始时间
    private Date beginDateTime;
    // 结束时间
    private Date endDateTime;
    // 库存总量
    private Integer stockCount;
    // 剩余库存
    private Integer stockCountSurplus;
    // 活动参与次数配置
    private Long activityCountId;
    // 抽奖策略ID
    private Long strategyId;
    // 活动状态
    private String state;
}
