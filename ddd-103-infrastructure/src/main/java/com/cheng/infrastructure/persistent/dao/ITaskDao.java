package com.cheng.infrastructure.persistent.dao;

import com.cheng.infrastructure.persistent.po.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/25 18:18
 */
@Mapper
public interface ITaskDao {
    void insert(Task task);

    void updateTaskSendMessageCompleted(Task task);

    void updateTaskSendMessageFail(Task task);

    List<Task> queryNoSendMessageTaskList();
}
