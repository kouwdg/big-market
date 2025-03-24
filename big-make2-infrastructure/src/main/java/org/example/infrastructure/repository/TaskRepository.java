package org.example.infrastructure.repository;




import lombok.extern.slf4j.Slf4j;
import org.example.domain.task.model.entity.TaskEntity;
import org.example.domain.task.repository.ITaskRepository;
import org.example.infrastructure.event.eventPublisher;
import org.example.infrastructure.persistent.dao.ITaskDao;
import org.example.infrastructure.persistent.po.Task;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 15:36
 */
@Repository
@Slf4j
public class TaskRepository implements ITaskRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private eventPublisher eventPublisher;
    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        List<Task> tasks = taskDao.queryNoSendMessageTaskList();
        List<TaskEntity> taskEntityList=new ArrayList<>();
        for(Task tem:tasks){
            TaskEntity taskEntity = TaskEntity.builder()
                        .userId(tem.getUserId())
                        .topic(tem.getTopic())
                        .message(tem.getMessage())
                        .messageId(tem.getMessageId())
                        .build();
            taskEntityList.add(taskEntity);
        }
        return taskEntityList;
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
      eventPublisher.publish(taskEntity.getTopic(),taskEntity.getMessage());
    }


    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
         taskDao.updateTaskSendMessageCompleted(Task.builder()
                .userId(userId)
                .messageId(messageId)
                .build());

    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        taskDao.updateTaskSendMessageFail(Task.builder()
                .userId(userId)
                .messageId(messageId)
                .build());

    }
}
