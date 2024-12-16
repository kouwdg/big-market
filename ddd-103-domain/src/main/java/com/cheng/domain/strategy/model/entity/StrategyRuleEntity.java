package com.cheng.domain.strategy.model.entity;

import com.cheng.types.common.Constants;
import com.cheng.types.enums.ResponseCode;
import com.cheng.types.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleEntity {

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


    //实例数据 4000:101,102,103,105 5000:106,107,108 6000:101,102,103,105,106,107,108,109
    public Map<String, List<Integer>> getRuleWeight(){
        if(!ruleModel.equals("rule_weight")) return null;
        //分隔 空隔
        String[] groups = ruleValue.split(Constants.BLANK);
        Map<String,List<Integer>> map=new HashMap<>();
        for (String group : groups) {
            //根据 : 分割
            String[] split = group.split(Constants.COLON);
            //将数据转换数字
            if(split.length!=2){throw new AppException(ResponseCode.RULE_WRIGHT.getCode());
            }
            String[] values = split[1].split(Constants.SPLIT);
            List<Integer> list=new ArrayList<>();
            for (String value : values) {
                int i = Integer.parseInt(value);
                list.add(i);
            }
            map.put(split[0],list);
        }
        return map;
    }

    public boolean isRuleWeight(){
        String[] split = ruleModel.split(Constants.SPLIT);
        for (String s : split) {
            if(s.equals("rule_weight"))return true;
        }
        return false;
    }
}
