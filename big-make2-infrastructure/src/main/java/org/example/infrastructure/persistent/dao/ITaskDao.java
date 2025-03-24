package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.Task;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/20 19:43
 */
@Mapper
public interface ITaskDao {
    void insert(Task task);

    //更新 任务状态 为 已发送
    void updateTaskSendMessageCompleted(Task task);

    //更新 任务状态 为 失败
    void updateTaskSendMessageFail(Task task);

    //查询没有发送成功的任务
    List<Task> queryNoSendMessageTaskList();
}
