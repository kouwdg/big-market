package org.example.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.example.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 黑名单责任链
 * @date 2025/2/15 13:56
 */
@Component("rule_blacklist")
@Slf4j
public class BackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Override
    public DefaultChainFactory.StrategyAwardIdVo raffle(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始");

        //查询用户是否在黑名单中
        String ruleValue=repository.queryStrategyRuleValue(strategyId,ruleModel());

        String[] split = ruleValue.split(Constants.COLON);
        int awardId = Integer.parseInt(split[0]);
        String[] blackUsers = split[1].split(Constants.SPLIT);
        //在 接管
        for (String tem : blackUsers) {
            if (userId.equals(tem)){
                log.info("抽奖责任链-黑名单接管 ");
                return DefaultChainFactory.StrategyAwardIdVo.builder()
                        .awardId(awardId)
                        .logicModel(ruleModel())
                        .build();
            }
        }
        //不在 放行
        log.info("抽奖责任链-黑名单放行");
        return Next().raffle(userId,strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }
}
