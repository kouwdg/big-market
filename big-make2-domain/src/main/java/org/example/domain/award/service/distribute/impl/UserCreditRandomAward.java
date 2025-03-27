package org.example.domain.award.service.distribute.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.domain.activity.model.vo.UserAwardRecordStateVo;
import org.example.domain.activity.repository.IAwardRepository;
import org.example.domain.award.model.aggregate.GiveOutPrizesAggregate;
import org.example.domain.award.model.entity.DistributeAwardEntity;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.entity.UserCreditAwardEntity;
import org.example.domain.award.service.distribute.IDistributeAward;
import org.example.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 发放奖品类型为积分的奖品
 * @date 2025/3/25 17:11
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {
    @Resource
    private IAwardRepository awardRepository;

    //发奖
    @Override
    public void giveOutPrizes(DistributeAwardEntity entity) {
        return;
    }

    //发放积分类型的奖品
    @Override
    public void distributeAward(DistributeAwardEntity entity) {
        Integer awardId = entity.getAwardId();
        String awardConfig = entity.getAwardConfig();
        if (StringUtils.isBlank(awardConfig)) {
            awardConfig = awardRepository.queryAwardConfig(awardId);
        }
        String[] split = awardConfig.split(Constants.SPLIT);
        if (split.length != 2) {
            throw new RuntimeException("积分制配置错误");
        }
        //生成随机积分值
        BigDecimal creditAmount = generateRandom(new BigDecimal(split[0]), new BigDecimal(split[1]));

        //构建聚合对象
        UserAwardRecordEntity userAwardRecordEntity = UserAwardRecordEntity.builder()
                .userId(entity.getUserId())
                .orderId(entity.getOrderId())
                .awardId(entity.getAwardId())
                .awardState(UserAwardRecordStateVo.create)
                .build();
        UserCreditAwardEntity userCreditAwardEntity = UserCreditAwardEntity.builder()
                .userId(entity.getUserId())
                .creditAmount(creditAmount)
                .build();

        GiveOutPrizesAggregate giveOutPrizesAggregate = GiveOutPrizesAggregate.builder()
                .userId(entity.getUserId())
                .userAwardRecordEntity(userAwardRecordEntity)
                .userCreditAwardEntity(userCreditAwardEntity)
                .build();

        //存储发奖对象
        awardRepository.saveGiveOutPrizesAggregate(giveOutPrizesAggregate);

    }

    private BigDecimal  generateRandom(BigDecimal min,BigDecimal max){
        //如果两个积分值相等
        if (min.equals(max))return min;
        BigDecimal randomBifDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBifDecimal.round(new MathContext(3));

    }
}
