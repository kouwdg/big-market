package com.cheng.domain.strategy.model.entity;

import com.cheng.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyEntity {
    //策略id
    private Long strategyId;
    //抽奖策略描述
    private String strategyDesc;
    //抽奖规则模型
    private String ruleModels;

    //获得 ruleModel中的 rule_weight
    //示例数据 ：rule_weight,rule_backlist
    public String getRuleWeight(){
        if(ruleModels==null||ruleModels.length()==0){
            return null;
        }
        String[] split = ruleModels.split(Constants.SPLIT);
        for (String string : split) {
            if("rule_weight".equals(string)){
                return string;
            }
        }
        return null;
    }

    public String[] getRuleModelList(){
        if(ruleModels==null||ruleModels.length()==0){
            return null;
        }
        return ruleModels.split(Constants.SPLIT);
    }
}
