package com.cheng.infrastructure.persistent.repository;

import com.alibaba.fastjson2.JSON;
import com.cheng.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.cheng.domain.award.model.entity.TaskEntity;
import com.cheng.domain.award.model.entity.UserAwardRecordEntity;
import com.cheng.domain.award.repository.IAwardRepository;
import com.cheng.infrastructure.event.eventPublisher;
import com.cheng.infrastructure.persistent.dao.ITaskDao;
import com.cheng.infrastructure.persistent.dao.IUserAwardRecordDao;
import com.cheng.infrastructure.persistent.po.Task;
import com.cheng.infrastructure.persistent.po.UserAwardRecord;
import com.cheng.types.exception.AppException;
import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 14:55
 */
@Repository
@Slf4j
public class AwardRepository implements IAwardRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private IUserAwardRecordDao userAwardRecordDao;

    @Resource
    private eventPublisher eventPublisher;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();

        // 奖品流水存储
        UserAwardRecord userAwardRecord= UserAwardRecord.builder()
                .userId(userAwardRecordEntity.getUserId())
                .activityId(userAwardRecordEntity.getActivityId())
                .strategyId(userAwardRecordEntity.getStrategyId())
                .orderId(userAwardRecordEntity.getOrderId())
                .awardId(userAwardRecordEntity.getAwardId())
                .awardTitle(userAwardRecordEntity.getAwardTitle())
                .awardTime(userAwardRecordEntity.getAwardTime())
                .awardState(userAwardRecordEntity.getAwardState().getCode())
                .build();

        // 任务表存储
        Task task= Task.builder()
                .userId(taskEntity.getUserId())
                .topic(taskEntity.getTopic())
                .message(JSON.toJSONString(taskEntity.getMessage()))
                .messageId(taskEntity.getMessageId())
                .state(taskEntity.getState().getCode())
                .build();

        //写入数据库
        saveTaskAndUserAwardRecord(userAwardRecord,task);

        //发送MQ消息
        try {
            eventPublisher.publish(task.getTopic(),task.getMessage());
            //更新task的状态
            taskDao.updateTaskSendMessageCompleted(task);
        }catch (Exception e){
            log.info("写入中奖记录，发送MQ消息失败 userId:{} topic:{}",task.getUserId(),task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveTaskAndUserAwardRecord(UserAwardRecord userAwardRecord, Task task) {
        try {
            // 写入流水记录
            userAwardRecordDao.insert(userAwardRecord);
            // 写入任务
            taskDao.insert(task);
        }catch (Exception e){
            log.error("写入出错",e);
            throw new AppException("写入出错");
        }

    }


}
