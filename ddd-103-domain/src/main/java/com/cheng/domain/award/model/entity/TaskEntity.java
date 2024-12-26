package com.cheng.domain.award.model.entity;

import com.cheng.domain.award.event.SendAwardMessageEvent;
import com.cheng.domain.award.model.vo.TaskStateVo;
import com.cheng.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 11:09
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    /* 用户ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息主体 */
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;
    /* 消息ID */
    private String messageId;
    /** 任务状态；create-创建、completed-完成、fail-失败 */
    private TaskStateVo state;
}
