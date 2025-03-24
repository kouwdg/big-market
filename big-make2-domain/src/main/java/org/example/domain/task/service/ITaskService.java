package org.example.domain.task.service;



import org.example.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 15:32
 */
public interface ITaskService {

    //查看是否有未发送的task
    List<TaskEntity> queryNoSendMessageTaskList();

    boolean sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);
}
