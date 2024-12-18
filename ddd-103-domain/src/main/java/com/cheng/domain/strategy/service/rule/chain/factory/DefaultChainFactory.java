package com.cheng.domain.strategy.service.rule.chain.factory;

import com.cheng.domain.strategy.model.entity.StrategyEntity;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.rule.chain.ILogicChain;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j

@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainMap;

    private IStrategyRepository repository;

    @Autowired
    public DefaultChainFactory(Map<String, ILogicChain> logicChainMap, IStrategyRepository repository) {
        this.logicChainMap = logicChainMap;
        this.repository = repository;
    }

    public ILogicChain openLogicChain(Long strategyId){
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategyEntity.getRuleModelList();
        if(ruleModels==null||ruleModels.length==0){
            log.info("前置规则为空");
            return logicChainMap.get("default");
        }
        ILogicChain iLogicChain = logicChainMap.get(ruleModels[0]);
        ILogicChain current=iLogicChain;
        for(int i=1;i<ruleModels.length;i++){
            ILogicChain nextChain = logicChainMap.get(ruleModels[i]);
            current=current.appendNext(nextChain);
        }
        current.appendNext(logicChainMap.get("default"));
        return iLogicChain;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardIdVo{
        private Integer awardId;
        private String logicModel;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel{
        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重规则"),
        ;
        private final String code;
        private final String info;
    }
}
