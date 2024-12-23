package com.cheng.test;

import com.alibaba.fastjson.JSON;
import com.cheng.infrastructure.persistent.dao.IRaffleActivityDao;
import com.cheng.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import com.cheng.infrastructure.persistent.po.RaffleActivity;
import com.cheng.infrastructure.persistent.po.RaffleActivityOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Test
    public void test() {
        RaffleActivity raffleActivity =
                raffleActivityDao.queryRaffleActivity(132L);
        log.info("数据:{}", JSON.toJSONString(raffleActivity));

        RaffleActivityOrder order = new RaffleActivityOrder();
        order.setUserId("chengyule");
        order.setActivityId(10030L);
        order.setActivityName("测试");
        order.setStrategyId(100006L);
        order.setOrderId(RandomStringUtils.randomNumeric(12));
        order.setOrderTime(new Date());
        order.setState("not_used");

        //
        raffleActivityOrderDao.insert(order);
    }

}
