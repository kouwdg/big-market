package com.cheng.domain.strategy.service.raffle;
import com.cheng.domain.strategy.model.entity.RaffleAwardEntity;
import com.cheng.domain.strategy.model.entity.RaffleFactorEntity;
import com.cheng.domain.strategy.model.entity.RuleActionEntity;
import com.cheng.domain.strategy.model.entity.StrategyAwardEntity;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.IRaffleStock;
import com.cheng.domain.strategy.service.IRaffleStrategy;
import com.cheng.domain.strategy.service.armory.IStrategyDispatch;
import com.cheng.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.cheng.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.cheng.types.enums.ResponseCode;
import com.cheng.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    protected IStrategyRepository repository;
    protected IStrategyDispatch strategyDispatch;

    protected final DefaultChainFactory defaultChainFactory;

    protected final DefaultTreeFactory defaultTreeFactory;

    @Autowired
    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
    }

    
    @Override
    public RaffleAwardEntity preformRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1 校验参数  检查用户传递进来的 userId StrategyId
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(userId==null||strategyId==null){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        log.info("校验参数成功");

        //2 使用 责任链模式 处理 前置规则
        DefaultChainFactory.StrategyAwardIdVo strategyAwardIdVo = raffleLogicChain(userId, strategyId);
        log.info(strategyAwardIdVo.getLogicModel());
        //进行了 黑名单 权重 处理时
        if(!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(strategyAwardIdVo.getLogicModel())){
            log.info("进来了");
            return buildRaffleAwardEntity(strategyId,strategyAwardIdVo.getAwardId());
        }

        Integer awardId=strategyAwardIdVo.getAwardId();
        log.info("前置规则处理完成，获得奖品:{}",awardId);

        //todo 不是默认的抽奖就直接返回

        //3 使用 规则树 处理 中置规则
        DefaultTreeFactory.StrategyAwardData strategyAwardData = raffleLogicTree(userId, strategyId, awardId);
        log.info("策略树处理完成,获得奖品:{}",strategyAwardData.getAwardId());

        return buildRaffleAwardEntity(strategyId,strategyAwardData.getAwardId());
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity build,String...logic);
    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity build,String...logic);

    protected abstract DefaultChainFactory.StrategyAwardIdVo raffleLogicChain(String userId,Long StrategyId);

    protected abstract DefaultTreeFactory.StrategyAwardData raffleLogicTree(String userId,Long StrategyId,Integer awardId);

    private RaffleAwardEntity buildRaffleAwardEntity(Long strategyId,Integer awardId){
        StrategyAwardEntity entity = repository.queryStrategyAwardEntity(strategyId, awardId);
        return RaffleAwardEntity.builder()
                .awardId(entity.getAwardId())
                .sort(entity.getSort())
                .build();
    }

}
