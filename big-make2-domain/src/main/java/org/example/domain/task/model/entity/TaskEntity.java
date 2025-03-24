package org.example.domain.task.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 15:33
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
    private String message;
    /* 消息ID */
    private String messageId;

}