package com.cheng.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.cheng.domain.strategy.model.entity.RaffleAwardEntity;
import com.cheng.domain.strategy.model.entity.RaffleFactorEntity;
import com.cheng.domain.strategy.model.entity.StrategyEntity;
import com.cheng.domain.strategy.model.entity.StrategyRuleEntity;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.IRaffleStrategy;
import com.cheng.domain.strategy.service.armory.IStrategyArmory;
import com.cheng.domain.strategy.service.armory.IStrategyDispatch;
import com.cheng.infrastructure.persistent.dao.IStrategyAwardDao;
import com.cheng.infrastructure.persistent.dao.IStrategyDao;
import com.cheng.infrastructure.persistent.po.StrategyAward;
import com.cheng.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.security.SecureRandom;

import java.util.List;
import java.util.Map;


@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class StrategyAwardTest {
    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRedisService redisService;

    @Resource
    private IStrategyRepository repository;

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Test
    public void select(){
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList();
        for (StrategyAward award : strategyAwards) {
            log.info("awardId:{},info:{}",award.getAwardId(), JSON.toJSONString(award));
        }
    }

    @Test
    public void redis(){
    }

    @Test
    public void test2(){
        //装配表格，放到redis中
        strategyArmory.assembleLotteryStrategy(10002L);

    }

    @Test
    public void test3(){
        RaffleFactorEntity raffleFactorEntity = new RaffleFactorEntity();
        raffleFactorEntity.setStrategyId(10001L);
        raffleFactorEntity.setUserId("user3");

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.preformRaffle(raffleFactorEntity);

        log.info("奖品实体类:{}",raffleAwardEntity);
        log.info("奖品Id:{}",raffleAwardEntity.getAwardId());

    }



}
