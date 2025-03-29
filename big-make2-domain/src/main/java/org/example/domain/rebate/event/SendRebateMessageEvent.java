package org.example.domain.rebate.event;

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
 * @description: TODO
 * @date 2024/12/30 21:39
 */

@Component
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.RebateMessage> {

    private final String topic = "send_rebate";

    @Override
    public EventMessage<RebateMessage> buildEventMessage(RebateMessage data) {
        return EventMessage.<RebateMessage>builder()
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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RebateMessage {
        /* 用户ID*/
        private String userId;
        /**
         * 返利描述
         */
        private String rebateDesc;
        /**
         * 返利类型（sku 添加抽奖次数、integral 用户活动积分）
         */
        private String rebateType;
        /**
         * 返利配置【添加抽奖的次数，积分值】
         */
        private String rebateConfig;
        /* 业务Id - 确保唯一值*/
        private String bizId;

        private String orderId;
        private Long activityId;
    }
}
