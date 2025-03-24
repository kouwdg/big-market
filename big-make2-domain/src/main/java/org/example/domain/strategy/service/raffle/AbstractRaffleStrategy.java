package org.example.domain.strategy.service.raffle;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IStrategyDispatch;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import javax.annotation.Resource;


@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {


    @Resource
    protected IStrategyRepository repository;


    @Resource
    protected DefaultChainFactory defaultChainFactory;

    @Resource
    protected  DefaultTreeFactory defaultTreeFactory;

    @Override
    public RaffleAwardEntity preformRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1 校验参数  检查用户传递进来的 userId StrategyId
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(userId==null||strategyId==null){
            //todo 抛出异常
           return null;
        }
        log.info("校验参数成功");

        //2 使用 责任链模式 处理 前置规则
        DefaultChainFactory.StrategyAwardIdVo strategyAwardIdVo = raffleLogicChain(userId, strategyId);

        //进行了 黑名单 权重 处理时
        if(!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(strategyAwardIdVo.getLogicModel())){
            log.info("进来了");
            return buildRaffleAwardEntity(strategyId,strategyAwardIdVo.getAwardId());
        }

        Integer awardId=strategyAwardIdVo.getAwardId();
        log.info("前置规则处理完成，获得奖品:{}",awardId);

        //3 使用 规则树 处理 奖品的 规则
        DefaultTreeFactory.StrategyAwardData strategyAwardData = raffleLogicTree(userId, strategyId, awardId);
        log.info("策略树处理完成,获得奖品:{}",strategyAwardData.getAwardId());
        return buildRaffleAwardEntity(strategyId,strategyAwardData.getAwardId());
    }

    protected abstract DefaultChainFactory.StrategyAwardIdVo raffleLogicChain(String userId,Long StrategyId);

    protected abstract DefaultTreeFactory.StrategyAwardData raffleLogicTree(String userId,Long StrategyId,Integer awardId);

    private RaffleAwardEntity buildRaffleAwardEntity(Long strategyId,Integer awardId){
        StrategyAwardEntity entity = repository.queryStrategyAwardEntity(strategyId, awardId);
        return RaffleAwardEntity.builder()
                .awardTitle(entity.getAwardTitle())
                .awardId(entity.getAwardId())
                .sort(entity.getSort())
                .build();
    }


}
