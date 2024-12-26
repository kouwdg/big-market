package com.cheng.infrastructure.persistent.repository;



import com.cheng.domain.task.model.entity.TaskEntity;
import com.cheng.domain.task.repository.ITaskRepository;
import com.cheng.infrastructure.event.eventPublisher;
import com.cheng.infrastructure.persistent.dao.ITaskDao;
import com.cheng.infrastructure.persistent.po.Task;
import lombok.extern.slf4j.Slf4j;
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
