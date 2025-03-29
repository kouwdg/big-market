package org.example.trigger.listener;

import com.alibaba.fastjson2.JSON;

import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.RechargeDraw.Model.entity.AddUserDrawEntity;
import org.example.domain.RechargeDraw.Service.IRechargeDrawService;
import org.example.domain.activity.service.RaffleOrder.IRaffleOrder;
import org.example.domain.credit.model.Enum.TradeNameVo;
import org.example.domain.credit.model.Enum.TradeTypeVo;
import org.example.domain.credit.model.entity.TradeEntity;
import org.example.domain.credit.service.ICreditAdjustService;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.entity.skuRechargeEntity;
import org.example.domain.rebate.model.vo.RebateTypeVo;
import org.example.types.event.BaseEvent;
import org.example.types.exception.AppException;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 处理活动返利的 消费队列
 * @date 2025/1/3 21:18
 */
@Slf4j
@Component
public class RebateMessageCustomer {

    private final String topic = "send_rebate";

    @Resource
    private IRaffleOrder raffleOrder;
    @Resource
    private ICreditAdjustService creditAdjustService;
    @Resource
    private IRechargeDrawService rechargeDrawService;


    //处理 签到返利 消息
    @RabbitListener(queuesToDeclare = @Queue(value = "send_rebate"))
    public void listener(String message) {
        try {
            //转换对象
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage =
                    JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
                    }.getType());

            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();
            if (!RebateTypeVo.SKU.getCode().equals(rebateMessage.getRebateType())) {
                log.info("监听用户行为返利消息非sku奖励暂时不处理 topic{},message{}", topic, message);
                return;
            }

            String rebateType = rebateMessage.getRebateType();
            switch (rebateType) {
                //添加抽奖次数
                case "sku":
                    rechargeDrawService.addUserDrawTimes(AddUserDrawEntity.builder()
                                    .userId(rebateMessage.getUserId())
                                    .orderId(rebateMessage.getOrderId())
                                    .activityId(rebateMessage.getActivityId())
                                    .count(Integer.parseInt(rebateMessage.getRebateConfig()))
                            .build());
                    break;
                    //添加积分
                case "integral":
                    TradeEntity build = TradeEntity.builder()
                            .userId(rebateMessage.getUserId())
                            .adjustAmount(new BigDecimal(rebateMessage.getRebateConfig()))
                            .tradeType(TradeTypeVo.REBATE.getName())
                            .tradeName(TradeNameVo.REBATE.getName())
                            .outBusinessNo(RandomStringUtils.randomNumeric(12))
                            .build();
                    creditAdjustService.creteOrder(build);
                    break;
            }
        } catch (AppException e) {
            log.error("监听用户行为返利消息，消费失败 :{}", e.getInfo());
            return;
        } catch (Exception e) {
            log.error("监听用户行为返利消息，消费失败 topic:{},message:{}", topic, message);
            return;
        }
    }
}
