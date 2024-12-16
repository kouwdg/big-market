package com.cheng.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class strategyRule {

    //自增id
    private Long id;
    //策略id
    private Long strategyId;
    //抽奖奖品Id
    private Integer awardId;
    //抽奖规则类型
    private Integer ruleType;
    //抽奖规则类型
    private String ruleModel;
    //抽奖规则比值
    private String ruleValue;
    //抽奖规则描述
    private String ruleDesc;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}
