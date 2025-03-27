package org.example.domain.activity.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.types.event.BaseEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 定义消息体
 * @date 2024/12/26 14:22
 */
@Component
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.SendAwardMessage> {


    private final String topic="send_award";

    @Override
    public EventMessage<SendAwardMessage> buildEventMessage(SendAwardMessage data) {
        return EventMessage.<SendAwardMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SendAwardMessage{
        private String userId;
        private Integer awardId;
        private String orderId;
        private String awardTitle;
    }
}
