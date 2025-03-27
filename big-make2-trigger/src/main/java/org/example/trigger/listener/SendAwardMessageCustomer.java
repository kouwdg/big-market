package org.example.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.event.SendAwardMessageEvent;
import org.example.domain.award.model.entity.DistributeAwardEntity;
import org.example.domain.award.service.distribute.IAwardDistributionService;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.types.event.BaseEvent;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 处理 发奖行为的 队列
 * @date 2025/3/27 11:26
 */

@Slf4j
@Component
public class SendAwardMessageCustomer {

    private final String topic = "send_award";

    @Resource
    private IAwardDistributionService AwardDistributionService;

    @RabbitListener(queuesToDeclare = @Queue(value = "send_award"))
    public void listener(String message) {
        try {
            //转换对象
            BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage =
                    JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {
                    }.getType());

            SendAwardMessageEvent.SendAwardMessage data = eventMessage.getData();
            System.out.println(data);


            //2 发奖
            DistributeAwardEntity build = DistributeAwardEntity.builder()
                    .userId(data.getUserId())
                    .awardId(data.getAwardId())
                    .orderId(data.getOrderId()).build();
            AwardDistributionService.distributeAward(build);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
