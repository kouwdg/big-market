package org.example.domain.activity.service.awardOrder;

import org.example.domain.activity.event.SendAwardMessageEvent;
import org.example.domain.activity.model.aggregate.UserAwardRecordAggregate;
import org.example.domain.activity.model.entity.TaskEntity;
import org.example.domain.activity.model.entity.UserAwardRecordEntity;
import org.example.domain.activity.model.vo.TaskStateVo;
import org.example.domain.activity.repository.IAwardRepository;
import org.example.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 保存奖品实现
 * @date 2024/12/26 14:38
 */
@Service
public class AwardService implements IAwardService{

    @Resource
    private IAwardRepository awardRepository;

    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        //1 构建消息实体对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        //2 构建任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setState(TaskStateVo.create);

        //构建聚合函数
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .taskEntity(taskEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .build();

        //存储聚合对象
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);

    }
}
