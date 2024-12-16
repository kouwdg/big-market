package com.cheng.domain.strategy.model.vo;

import com.cheng.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.cheng.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVo {
    private String ruleModel;

    public String[] raffleCenterRuleModelList(){
        List<String> list=new ArrayList<>();
        String[] split = ruleModel.split(Constants.SPLIT);
        for (String s : split) {
            if(DefaultLogicFactory.LogicModel.isCenter(s))list.add(s);
        }
        return list.toArray(new String[0]);
    }

    public String[] raffleAfterRuleModelList(){
        List<String> list=new ArrayList<>();
        String[] split = ruleModel.split(Constants.SPLIT);
        for (String s : split) {
            if(DefaultLogicFactory.LogicModel.isCenter(s))list.add(s);
        }
        return list.toArray(new String[0]);
    }
}
