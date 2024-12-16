package com.cheng.test.infrastructure;

import com.cheng.domain.strategy.model.entity.RaffleAwardEntity;
import com.cheng.domain.strategy.model.entity.RaffleFactorEntity;
import com.cheng.domain.strategy.service.IRaffleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class RaffleStrategyTest {

    @Resource
    private IRaffleStrategy raffleStrategy;


    @Test
    public void test2(){
        System.out.println("23");
    }
    @Test
    public void test_preformRaffle_blacklist(){
        RaffleFactorEntity user2 = RaffleFactorEntity.builder()
                .userId("user2")
                .strategyId(10001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.preformRaffle(user2);
        log.info("策略ID:{}",raffleAwardEntity.getStrategyId());
        log.info("奖品ID:{}",raffleAwardEntity.getAwardId());
    }

    @Test
    public void test_preformRaffle_ruleWeight() throws InterruptedException {
        RaffleFactorEntity user2 = RaffleFactorEntity.builder()
                .userId("user7")
                .strategyId(10002L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.preformRaffle(user2);
        log.info("策略ID:{}",raffleAwardEntity.getStrategyId());
        log.info("奖品ID:{}",raffleAwardEntity.getAwardId());

        new CountDownLatch(1).await();
    }
}
