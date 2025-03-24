package org.example;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.api.IRaffleActivityService;
import org.example.api.dto.RaffleStrategyRuleWeightRequestDTO;
import org.example.api.dto.RaffleStrategyRuleWeightResponseDTO;
import org.example.domain.activity.service.RaffleOrder.rule.chain.IActionChain;
import org.example.domain.activity.service.RaffleOrder.rule.chain.factory.DefaultActivityChainFactory;
import org.example.domain.activity.service.quota.IRaffleActivityAccountQuota;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.model.vo.RuleWeightVo;
import org.example.domain.strategy.model.vo.ruleTree.RuleTreeVO;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IStrategyDispatch;
import org.example.domain.strategy.service.armory.StrategyArmory;
import org.example.domain.strategy.service.award.IRaffleAward;
import org.example.domain.strategy.service.raffle.IRaffleStrategy;
import org.example.domain.strategy.service.rule.IRaffleRule;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.example.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import org.example.infrastructure.persistent.dao.IStrategyAwardDao;
import org.example.infrastructure.persistent.dao.IStrategyDao;
import org.example.infrastructure.persistent.dao.IStrategyRuleDao;
import org.example.infrastructure.persistent.po.Strategy;
import org.example.infrastructure.persistent.po.StrategyAward;
import org.example.infrastructure.persistent.po.StrategyRule;
import org.example.types.model.Response;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/11 22:58
 */
@SpringBootTest
@Slf4j
public class Test1 {



}
