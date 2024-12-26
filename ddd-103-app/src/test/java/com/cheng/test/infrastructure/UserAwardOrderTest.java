package com.cheng.test.infrastructure;

import com.cheng.domain.award.model.entity.UserAwardRecordEntity;
import com.cheng.domain.award.model.vo.UserAwardRecordStateVo;
import com.cheng.domain.award.service.IAwardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 16:33
 */

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class UserAwardOrderTest {

    @Resource
    private IAwardService awardService;

    @Test
    public void test1(){
        for(int i=0;i<100;i++){
            UserAwardRecordEntity userAwardRecordEntity = new UserAwardRecordEntity();
            userAwardRecordEntity.setUserId("cheng");
            userAwardRecordEntity.setActivityId(101L);
            userAwardRecordEntity.setOrderId(RandomStringUtils.randomNumeric(11));
            userAwardRecordEntity.setAwardId(100);
            userAwardRecordEntity.setAwardTime(new Date());
            userAwardRecordEntity.setAwardTitle("键盘");
            userAwardRecordEntity.setAwardState(UserAwardRecordStateVo.create);
            userAwardRecordEntity.setStrategyId(100006L);
            awardService.saveUserAwardRecord(userAwardRecordEntity);
        }
    }
}
