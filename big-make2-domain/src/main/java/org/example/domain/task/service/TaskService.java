package org.example.domain.task.service;



import org.example.domain.task.model.entity.TaskEntity;
import org.example.domain.task.repository.ITaskRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 15:39
 */
@Service
public class TaskService implements ITaskService {

    @Resource
    private ITaskRepository taskRepository;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }

    @Override
    public boolean sendMessage(TaskEntity taskEntity) {
        try {
            taskRepository.sendMessage(taskEntity);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
         taskRepository.updateTaskSendMessageCompleted(userId,messageId);

    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
      taskRepository.updateTaskSendMessageFail(userId,messageId);
    }
}
