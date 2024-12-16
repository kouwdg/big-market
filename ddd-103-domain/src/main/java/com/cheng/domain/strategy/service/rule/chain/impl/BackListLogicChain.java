package com.cheng.domain.strategy.service.rule.chain.impl;

import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.cheng.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.cheng.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_blacklist")
public class BackListLogicChain extends AbstractLogicChain {
    @Resource
    protected IStrategyRepository repository;

    //抽奖
    @Override
    public DefaultChainFactory.StrategyAwardIdVo logic(String UserId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始 userId:{} strategyId:{} ruleModel:{}", UserId,strategyId,ruleModel());
        //查询是否在黑名单当中用户名
        String ruleValue=repository.queryStrategyRuleValue(strategyId,ruleModel());

        String[] split = ruleValue.split(Constants.COLON);
        int awardId = Integer.parseInt(split[0]);
        //获得黑名单中的用户信息
        String[] blackUsers = split[1].split(Constants.SPLIT);
        for (String blackUser : blackUsers) {
            if(blackUser.equals(UserId)){
                log.info("抽奖责任链-黑名单接管 userId:{} strategyId:{} ruleModel:{} awardId:{}",
                        UserId,strategyId,ruleModel(),awardId);
                return DefaultChainFactory.StrategyAwardIdVo.builder()
                        .awardId(awardId)
                        .logicModel(ruleModel())
                        .build();
            }
        }
        log.info("抽奖责任链-黑名单放行 userId:{} strategyId:{} ruleModel:{}",
                UserId,strategyId,ruleModel());
        log.info("测试git");
        return next().logic(UserId,strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }
}
