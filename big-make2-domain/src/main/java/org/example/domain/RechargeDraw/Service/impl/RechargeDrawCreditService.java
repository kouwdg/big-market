package org.example.domain.RechargeDraw.Service.impl;
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
 * @description: 根据积分兑换抽奖次数
 * @date 2025/3/28 11:35
 */

@Service("Recharge_draw_credit")
public class RechargeDrawCreditService extends AbstractRechargeDrawService {

    @Override
    public void RechargeDraw(RechargeDrawVo drawVo) {
        //1 判断用户的积分是否足够
        Boolean enough = rechargeDrawReposition.IsCreditEnough(drawVo.getUserId(), drawVo.getSku(), drawVo.getCount());
        if (enough) {
            //获取RaffleActivityOrderEntity
            RaffleActivityEntity raffleActivityEntity = rechargeDrawReposition.queryRaffleActivityEntity(drawVo.getActivityId());
            RaffleActivityOrderEntity raffleActivityOrderEntity = RechargeDrawAggregate.CreateRaffleActivityOrderEntity(
                    drawVo.getUserId(),
                    drawVo.getSku(),
                    raffleActivityEntity.getActivityId(),
                    raffleActivityEntity.getActivityName(),
                    raffleActivityEntity.getStrategyId(),
                    raffleActivityEntity.getState(),
                    drawVo.getCount());
            //获得userCreditAwardEntity
            BigDecimal productAmount = rechargeDrawReposition.queryProductAmount(drawVo.getSku());
            UserCreditAwardEntity userCreditAwardEntity = RechargeDrawAggregate.CreateUserCreditAwardEntity(drawVo.getUserId(), productAmount.multiply(new BigDecimal(drawVo.getCount())));

            //获得taskEntity
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = new BaseEvent.EventMessage<>();

            SendRebateMessageEvent.RebateMessage data = new SendRebateMessageEvent.RebateMessage();
            data.setUserId(drawVo.getUserId());
            data.setRebateType("sku");
            data.setRebateConfig(drawVo.getCount().toString());
            data.setBizId(raffleActivityOrderEntity.getOrderId());
            data.setOrderId(raffleActivityOrderEntity.getOrderId());
            data.setActivityId(drawVo.getActivityId());

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


            rechargeDrawReposition.saveRechageDrawAggregate(build);
        } else {
            throw new RuntimeException("用户积分不足");
        }


    }
}
