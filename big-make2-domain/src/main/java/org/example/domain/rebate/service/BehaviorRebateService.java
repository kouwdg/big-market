package org.example.domain.rebate.service;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.entity.DailyBehaviorRebateEntity;
import org.example.domain.rebate.model.entity.TaskEntity;
import org.example.domain.rebate.model.vo.TaskStateVo;
import org.example.domain.rebate.repository.IBehaviorRebateRepository;
import org.example.types.common.Constants;
import org.example.types.event.BaseEvent;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 行为返利服务实现
 * @date 2024/12/30 18:48
 */
@Service
public class BehaviorRebateService implements IBehaviorRebateService {

    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;

    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;
    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
        // 1 查询返利配置
        List<DailyBehaviorRebateEntity> dailyBehaviorRebateEntities = behaviorRebateRepository.queryDailyBehaviorRebate(behaviorEntity.getBehaviorTypeVo().getCode());
        if (dailyBehaviorRebateEntities == null || dailyBehaviorRebateEntities.isEmpty()) {
            return new ArrayList<>();
        }

        // 返回的订单ID
        List<String> orderId = new ArrayList<>();

        //2 构建聚合对象
        List<BehaviorRebateAggregate> aggregateList = new ArrayList<>();
        for (DailyBehaviorRebateEntity tem : dailyBehaviorRebateEntities) {
            //业务拼装Id 用户Id_返利类型_外部透彻业务Id
            String biz_id = behaviorEntity.getUserId() + Constants.UNDERLINE + tem.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();

            //构建 返利订单对象
            BehaviorRebateOrderEntity orderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(11))
                    .behaviorType(tem.getBehaviorType())
                    .rebateDesc(tem.getRebateDesc())
                    .rebateType(tem.getRebateType())
                    .rebateConfig(tem.getRebateConfig())
                    .bizId(biz_id)
                    .outBusinessNo(behaviorEntity.getOutBusinessNo())
                    .build();


            // MQ消息对象
            SendRebateMessageEvent.RebateMessage MQMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behaviorEntity.getUserId())
                    .rebateDesc(tem.getRebateDesc())
                    .rebateType(tem.getRebateType())
                    .rebateConfig(tem.getRebateConfig())
                    .bizId(biz_id)
                    .build();
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(MQMessage);


            //构建事件消息对象
            TaskEntity taskEntity = TaskEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .topic(sendRebateMessageEvent.topic())
                    .message(rebateMessageEventMessage)
                    .messageId(rebateMessageEventMessage.getId())
                    .state(TaskStateVo.create)
                    .build();


            //行为返利聚合对象
            BehaviorRebateAggregate aggregate = BehaviorRebateAggregate.builder()
                    .userId(behaviorEntity.getUserId())
                    .behaviorRebateOrderEntity(orderEntity)
                    .taskEntity(taskEntity)
                    .build();
            aggregateList.add(aggregate);

            orderId.add(orderEntity.getOrderId());
        }
        //3 根据聚合对象保存用户返利订单表
        behaviorRebateRepository.saveUseRebateRecord(aggregateList);
        return orderId;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByBusinessNo(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryOrderByBusinessNo(userId,outBusinessNo);
    }

}
