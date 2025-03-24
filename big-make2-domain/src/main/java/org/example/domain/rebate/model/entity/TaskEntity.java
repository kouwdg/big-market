package org.example.domain.rebate.model.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.vo.TaskStateVo;
import org.example.types.event.BaseEvent;

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
    private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
    /* 消息ID */
    private String messageId;
    /** 任务状态；create-创建、completed-完成、fail-失败 */
    private TaskStateVo state;
}
