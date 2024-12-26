package com.cheng.trigger.job;


import com.cheng.domain.task.model.entity.TaskEntity;
import com.cheng.domain.task.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 15:30
 */
@Slf4j
@Component
public class SendMessageTaskJob {

    @Resource
    private ITaskService taskService;
    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            log.info("定时检查没有发送的task");
            List<TaskEntity> taskEntityList = taskService.queryNoSendMessageTaskList();
            if (taskEntityList==null){
                log.info("没有发送失败的task");
                return;
            }
            for(TaskEntity task:taskEntityList){
                boolean b = taskService.sendMessage(task);
                if (b){
                    taskService.updateTaskSendMessageCompleted(task.getUserId(),task.getMessageId());
                }
                taskService.updateTaskSendMessageFail(task.getUserId(),task.getMessageId());
            }
        }catch (Exception e){
            log.error("定时任务,扫描MQ任务列表发送消息失败",e);
        }
    }
}
