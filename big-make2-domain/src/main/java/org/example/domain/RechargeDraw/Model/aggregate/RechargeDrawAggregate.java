package org.example.domain.RechargeDraw.Model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.RechargeDraw.Model.entity.RaffleActivityOrderEntity;
import org.example.domain.RechargeDraw.Model.entity.TaskEntity;
import org.example.domain.RechargeDraw.Model.entity.UserCreditAwardEntity;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.vo.TaskStateVo;
import org.example.types.common.Constants;
import org.example.types.event.BaseEvent;

import java.math.BigDecimal;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 充值抽奖的聚合函数
 * @date 2025/3/28 13:14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RechargeDrawAggregate {
    RaffleActivityOrderEntity raffleActivityOrderEntity;
    UserCreditAwardEntity userCreditAwardEntity;
    TaskEntity taskEntity;


    public static RaffleActivityOrderEntity CreateRaffleActivityOrderEntity
            (String userId,
             Long sku,
             Long activityId,
             String activityName,
             Long strategyId,
             String state,
             Integer skuCount) {
        return RaffleActivityOrderEntity.builder()
                .userId(userId)
                .sku(sku)
                .activityName(activityName)
                .activityId(activityId)
                .state(state)
                .skuCount(skuCount)
                .strategyId(strategyId)
                .orderId(RandomStringUtils.randomNumeric(12))
                .build();

    }

    public static  UserCreditAwardEntity CreateUserCreditAwardEntity(String userId, BigDecimal creditAmount){
        return UserCreditAwardEntity.builder()
                .userId(userId)
                .creditAmount(creditAmount)
                .build();
    }
    public static  TaskEntity CreateTaskEntity(String userId,BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message){
        return TaskEntity.builder()
                .userId(userId)
                .topic(Constants.MQName.SEND_REBATE)
                .message(message)
                .messageId(RandomStringUtils.randomNumeric(12))
                .state(TaskStateVo.create)
                .build();
    }
}
