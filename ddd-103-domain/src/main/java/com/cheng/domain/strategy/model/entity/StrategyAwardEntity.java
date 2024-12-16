package com.cheng.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardEntity {
    //抽奖策略Id
    private Long strategyId;
    //奖品Id
    private Integer awardId;
    //奖品的库存总量
    private Integer awardCount;
    //奖品的库存
    private Integer awardCountSurplus;
    //奖品的中奖概率
    private BigDecimal awardRate;
    //排序
    private Integer sort;
    //标题
    private String awardTitle;
    //副标题
    private String awardSubtitle;
}

