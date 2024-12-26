package com.cheng.infrastructure.event;

import com.alibaba.fastjson2.JSON;
import com.cheng.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 发送消息
 * @date 2024/12/25 15:04
 */
@Slf4j
@Component
public class eventPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?>eventMessage){
        try {
            String messageJson = JSON.toJSONString(eventMessage);
            rabbitTemplate.convertAndSend(topic,messageJson);
            log.info("发送MQ消息 topic:{} message{}",topic,messageJson);
        }catch (Exception e){
            log.info("发送MQ消息失败 topic:{}",topic);
            throw e;
        }
    }

    public void publish(String topic, String eventMessageJson){
        try {
            rabbitTemplate.convertAndSend(topic,eventMessageJson);
            log.info("发送MQ消息 topic:{} message{}",topic,eventMessageJson);
        }catch (Exception e){
            log.info("发送MQ消息失败 topic:{}",topic);
            throw e;
        }
    }

}
