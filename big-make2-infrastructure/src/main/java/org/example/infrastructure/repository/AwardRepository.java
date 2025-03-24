package org.example.infrastructure.repository;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.aggregate.UserAwardRecordAggregate;
import org.example.domain.activity.model.entity.TaskEntity;
import org.example.domain.activity.model.entity.UserAwardRecordEntity;
import org.example.domain.activity.repository.IAwardRepository;
import org.example.infrastructure.event.eventPublisher;
import org.example.infrastructure.persistent.dao.IUserRaffleOrderDao;
import org.example.infrastructure.persistent.po.UserRaffleOrder;
import org.example.infrastructure.persistent.dao.ITaskDao;
import org.example.infrastructure.persistent.dao.IUserAwardRecordDao;
import org.example.infrastructure.persistent.po.Task;
import org.example.infrastructure.persistent.po.UserAwardRecord;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/20 19:28
 */

@Slf4j
@Service
public class AwardRepository implements IAwardRepository {

    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;

    @Resource
    private eventPublisher eventPublisher;

    @Resource
    private IUserAwardRecordDao userAwardRecordDao;
    @Resource
    private ITaskDao taskDao;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();

        // 奖品流水存储
        UserAwardRecord userAwardRecord = UserAwardRecord.builder()
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
        Task task = Task.builder()
                .userId(taskEntity.getUserId())
                .topic(taskEntity.getTopic())
                .message(JSON.toJSONString(taskEntity.getMessage()))
                .messageId(taskEntity.getMessageId())
                .state(taskEntity.getState().getCode())
                .build();

        //抽奖订单更新数据
        UserRaffleOrder userRaffleOrder = new UserRaffleOrder();
        userRaffleOrder.setUserId(userAwardRecord.getUserId());
        userRaffleOrder.setOrderId(userAwardRecord.getOrderId());

        //写入数据库
        saveTaskAndUserAwardRecord(userAwardRecord, task, userRaffleOrder);

        //发送MQ消息
        try {
            eventPublisher.publish(task.getTopic(), task.getMessage());
            //更新task的状态
            taskDao.updateTaskSendMessageCompleted(task);
        } catch (Exception e) {
            log.info("写入中奖记录，发送MQ消息失败 userId:{} topic:{}", task.getUserId(), task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveTaskAndUserAwardRecord(UserAwardRecord userAwardRecord, Task task, UserRaffleOrder userRaffleOrder) {
        try {
            // 写入流水记录
            userAwardRecordDao.insert(userAwardRecord);
            // 写入任务
            taskDao.insert(task);
            //更新抽奖订单
            int count = userRaffleOrderDao.updateUserRaffleOrderStateUsed(userRaffleOrder);
            if (count != 1) {
                throw new AppException(ResponseCode.ACTIVITY_ORDER_ERROR.getCode(), ResponseCode.ACTIVITY_ORDER_ERROR.getInfo());
            }
        } catch (Exception e) {
            log.error("写入出错", e);
            throw new AppException("写入出错");
        }

    }
}
