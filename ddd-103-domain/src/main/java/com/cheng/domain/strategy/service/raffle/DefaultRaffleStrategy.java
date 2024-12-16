package com.cheng.domain.strategy.service.raffle;

import com.cheng.domain.strategy.model.entity.RaffleFactorEntity;
import com.cheng.domain.strategy.model.entity.RuleActionEntity;
import com.cheng.domain.strategy.model.entity.RuleMatterEntity;
import com.cheng.domain.strategy.model.entity.StrategyAwardEntity;
import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.model.vo.StrategyAwardRuleModelVo;
import com.cheng.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeVO;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.IRaffleAward;
import com.cheng.domain.strategy.service.IRaffleStock;
import com.cheng.domain.strategy.service.armory.IStrategyDispatch;
import com.cheng.domain.strategy.service.rule.ILogicFilter;
import com.cheng.domain.strategy.service.rule.chain.ILogicChain;
import com.cheng.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.cheng.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.cheng.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.cheng.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.cheng.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleAward, IRaffleStock {

    @Resource
    private DefaultLogicFactory logicFactory;

    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }


    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logic) {
        //logic是规则的名称的数组

        //拿到 规则过滤的实体对象 的 集合
        Map<String, ILogicFilter<RuleActionEntity.RaffleBeforeEntity>> logicFilterGroup = logicFactory.openLogicFilter();

        //从传入进来的规则中获取黑名单规则
        String ruleBackList=Arrays.stream(logic)
                .filter(str->str.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .findFirst()
                .orElse(null);

        //如果有黑名单规则
        if (StringUtils.isNoneBlank(ruleBackList)){
            //黑名单规则的实例
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());

            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();

            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
            //todo 不懂
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
            ruleMatterEntity.setRuleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());

            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            //如果这次抽奖受黑名单规则接管
            if(!RuleLogicCheckTypeVo.ALLOW.getCode().equals(ruleActionEntity.getCode()))
                return ruleActionEntity;

        }

        //取出黑名单规则，剩余的规则作为数组
        List<String> ruleList = Arrays.stream(logic)
                .filter(str -> !str.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .collect(Collectors.toList());
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity=null;


        for (String ruleModel : ruleList) {
            //根据规则获得规则的实体类
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(ruleModel);

            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
            //todo 不懂
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());

            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            log.info("抽奖前规则过滤: userId:{} ruleModel{} code:{} info:{}",raffleFactorEntity.getUserId(),raffleFactorEntity.getStrategyId(),ruleActionEntity.getCode(),ruleActionEntity.getInfo());
            //被接管就 返回 RuleActionEntity
            if(!RuleLogicCheckTypeVo.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
        }

        //没有被接管
        return ruleActionEntity;
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logic) {
        if(logic==null ||logic.length==0) return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVo.ALLOW.getCode())
                .info(RuleLogicCheckTypeVo.ALLOW.getInfo())
                .build();

        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntity=null;
        //
        Map<String, ILogicFilter<RuleActionEntity.RaffleCenterEntity>> logicFilterGroup = logicFactory.openLogicFilter();
        for (String ruleModel : logic) {
            //根据规则获得规则的实体类
            ILogicFilter<RuleActionEntity.RaffleCenterEntity> logicFilter = logicFilterGroup.get(ruleModel);

            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
            ruleMatterEntity.setAwardId(raffleFactorEntity.getAwardId());
            ruleMatterEntity.setRuleModel(ruleModel);

            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            log.info("抽奖中规则过滤: userId:{} ruleModel{} code:{} info:{}",raffleFactorEntity.getUserId(),raffleFactorEntity.getStrategyId(),ruleActionEntity.getCode(),ruleActionEntity.getInfo());
            //被接管就 返回 RuleActionEntity
            if(!RuleLogicCheckTypeVo.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
        }

        return null;
    }


    //责任链模式 处理 前置规则 接口
    @Override
    protected DefaultChainFactory.StrategyAwardIdVo raffleLogicChain(String userId, Long StrategyId) {
        //配置 ILogicChain
        ILogicChain iLogicChain = defaultChainFactory.openLogicChain(StrategyId);
        //进行前置规则处理
        return iLogicChain.logic(userId, StrategyId);
    }

    //规则树 处理 中置规则
    @Override
    protected DefaultTreeFactory.StrategyAwardData raffleLogicTree(String userId, Long StrategyId, Integer awardId) {
        //查看该奖品是否有 规则树
        StrategyAwardRuleModelVo strategyAwardRuleModelVo =
                repository.queryStrategyAwardRuleModel(StrategyId, awardId);
        if (strategyAwardRuleModelVo==null){
            log.info("奖品没有设置规则树");
            return DefaultTreeFactory.StrategyAwardData.builder().awardId(awardId).build();
        }
        log.info("奖品对应的规则：{}",strategyAwardRuleModelVo.getRuleModel());
        RuleTreeVO ruleTreeVO=
                repository.queryRuleTreeVoByTreeId(strategyAwardRuleModelVo.getRuleModel());
        IDecisionTreeEngine engine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return engine.process(userId, StrategyId, awardId);

    }

    //获取 奖品库存的消耗队列
    @Override
    public StrategyAwardStockKeyVo takeQueryValue() throws InterruptedException {
        return repository.takeQueryValue();
    }
    //更新奖品
    @Override
    public void updateStrategyAwardStock(Long strategy, Integer award) {
        repository.updateStrategyAwardStock(strategy,award);
    }

    //根据策略Id查询奖品列表
    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }
}
