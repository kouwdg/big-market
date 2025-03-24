package org.example.trigger.listener;

import com.alibaba.fastjson2.JSON;

import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.service.RaffleOrder.IRaffleOrder;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.entity.skuRechargeEntity;
import org.example.domain.rebate.model.vo.RebateTypeVo;
import org.example.types.event.BaseEvent;
import org.example.types.exception.AppException;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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


    //处理 签到返利 消息
    @RabbitListener(queuesToDeclare = @Queue(value = "send_rebate"))
    public void listener(String message) {
        try {
            //转换对象
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage =
                    JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());

            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();
            if(!RebateTypeVo.SKU.getCode().equals(rebateMessage.getRebateType())){
                log.info("监听用户行为返利消息非sku奖励暂时不处理 topic{},message{}",topic,message);
                return;
            }

            //入账
            skuRechargeEntity skuRechargeEntity = new skuRechargeEntity();
            skuRechargeEntity.setSku(Long.valueOf(rebateMessage.getRebateConfig()));
            skuRechargeEntity.setUserId(rebateMessage.getUserId());
            skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
            //添加用户的抽奖次数
            raffleOrder.createSkuRechargeOrder(skuRechargeEntity);

        }catch (AppException e){
            log.error("监听用户行为返利消息，消费失败 :{}",e.getInfo());
            return;
        }
        catch (Exception e) {
            log.error("监听用户行为返利消息，消费失败 topic:{},message:{}", topic, message);
            return;
        }
    }
}
