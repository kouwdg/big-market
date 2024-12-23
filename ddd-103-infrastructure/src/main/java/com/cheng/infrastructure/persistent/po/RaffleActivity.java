package com.cheng.infrastructure.persistent.po;

import javafx.scene.chart.PieChart;
import lombok.Data;

import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 抽奖活动表
 * @date 2024/12/19 11:01
 */

@Data
public class RaffleActivity {
    // 自增ID
    private Long id;
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
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
}

