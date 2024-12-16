package com.cheng.domain.strategy.model.entity;

import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {
    //规则是否被接管
    private String code= RuleLogicCheckTypeVo.ALLOW.getCode();
    private String info= RuleLogicCheckTypeVo.ALLOW.getInfo();
    //规则的名字
    private String ruleModel;
    //被接管以后需要的信息
    private T data;

    public static class RaffleEntity{
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    //抽奖前
    static public class RaffleBeforeEntity extends RaffleEntity{
        //策略Id
        private Long strategyId;
        //权重值key
        private String ruleWeightValue;
        //奖品id
        private Integer awardId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    //抽奖中
    static public class RaffleCenterEntity extends RaffleEntity{
    }

    //抽奖后
    static public class RaffleLaterEntity extends RaffleEntity{}




}
