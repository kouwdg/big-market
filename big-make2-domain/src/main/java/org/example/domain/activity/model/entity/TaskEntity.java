package org.example.domain.activity.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.event.SendAwardMessageEvent;
import org.example.domain.activity.model.vo.TaskStateVo;
import org.example.types.event.BaseEvent;

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
