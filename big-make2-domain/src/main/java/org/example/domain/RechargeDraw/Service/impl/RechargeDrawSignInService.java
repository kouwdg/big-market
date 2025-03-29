package org.example.domain.RechargeDraw.Service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.RechargeDraw.Model.aggregate.RechargeDrawAggregate;
import org.example.domain.RechargeDraw.Model.entity.*;
import org.example.domain.RechargeDraw.Model.vo.RechargeDrawVo;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 根据签到获得抽奖次数
 * @date 2025/3/28 11:36
 */

@Service("Recharge_draw_sign_in")
public class RechargeDrawSignInService extends AbstractRechargeDrawService {


    @Override
    public void RechargeDraw(RechargeDrawVo drawVo) {

        //1 创建抽奖订单表
        //获取RaffleActivityOrderEntity
        RaffleActivityEntity raffleActivityEntity = rechargeDrawReposition.queryRaffleActivityEntity(drawVo.getActivity());
        RaffleActivityOrderEntity raffleActivityOrderEntity = RechargeDrawAggregate.CreateRaffleActivityOrderEntity(
                drawVo.getUserId(),
                drawVo.getSku(),
                raffleActivityEntity.getActivityId(),
                raffleActivityEntity.getActivityName(),
                raffleActivityEntity.getStrategyId(),
                raffleActivityEntity.getState(),
                drawVo.getCount());

        //获得userCreditAwardEntity
        UserCreditAwardEntity userCreditAwardEntity =
                RechargeDrawAggregate.CreateUserCreditAwardEntity(drawVo.getUserId(),BigDecimal.ZERO);

        //获得taskEntity
        BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = new BaseEvent.EventMessage<>();

        SendRebateMessageEvent.RebateMessage data = new SendRebateMessageEvent.RebateMessage();
        data.setUserId(drawVo.getUserId());
        data.setRebateType("sku");
        data.setRebateConfig(drawVo.getCount().toString());
        data.setBizId(RandomStringUtils.randomNumeric(12));

        rebateMessageEventMessage.setData(data);
        TaskEntity taskEntity = RechargeDrawAggregate.CreateTaskEntity(
                drawVo.getUserId(),
                rebateMessageEventMessage
        );
        //构建RechargeDrawAggregate
        RechargeDrawAggregate build = RechargeDrawAggregate.builder()
                .raffleActivityOrderEntity(raffleActivityOrderEntity)
                .userCreditAwardEntity(userCreditAwardEntity)
                .taskEntity(taskEntity)
                .build();

        //保存每日签到
        rechargeDrawReposition.saveRechageDrawAggregateSignIn(build);
    }
}
