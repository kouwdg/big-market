package com.cheng.test.infrastructure;

import com.cheng.domain.activity.model.entity.skuRechargeEntity;
import com.cheng.domain.activity.service.IRaffleOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖活动订单单测
 * @create 2024-03-16 11:51
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleOrderTest {
    @Resource
    private IRaffleOrder raffleOrder;
    @Test
    public void test_createSkuRechargeOrder() {
        skuRechargeEntity skuRechargeEntity = new skuRechargeEntity();
        skuRechargeEntity.setUserId("chengyule");
        skuRechargeEntity.setSku(101L);
        // outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
        String random = RandomStringUtils.random(10);
        skuRechargeEntity.setOutBusinessNo(random);
        String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
        log.info("测试结果：{}", orderId);
    }
}