package com.cheng.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardEntity {
    //策略id
    private Long strategyId;
    //奖品ID
    private Integer awardId;
    //奖品发放策略
    private String awardKey;
    //奖品配置信息
    private String awardConfig;
    //奖品发放策略
    private String awardDesc;
    //奖品序号
    private Integer sort;

}
