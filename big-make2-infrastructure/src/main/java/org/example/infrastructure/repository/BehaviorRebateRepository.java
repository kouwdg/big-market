package org.example.infrastructure.repository;

import com.alibaba.fastjson2.JSON;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.entity.DailyBehaviorRebateEntity;
import org.example.domain.rebate.model.entity.TaskEntity;
import org.example.domain.rebate.repository.IBehaviorRebateRepository;
import org.example.infrastructure.event.eventPublisher;
import org.example.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import org.example.infrastructure.persistent.dao.ITaskDao;
import org.example.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import org.example.infrastructure.persistent.po.DailyBehaviorRebate;
import org.example.infrastructure.persistent.po.Task;
import org.example.infrastructure.persistent.po.UserBehaviorRebateOrder;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/1/3 19:48
 */

@Repository
@Slf4j
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

    @Resource
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;

    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;
    @Resource
    private eventPublisher eventPublisher;
    @Resource
    private ITaskDao taskDao;

    //查询返利配置
    @Override
    public List<DailyBehaviorRebateEntity> queryDailyBehaviorRebate(String behaviorType) {
        List<DailyBehaviorRebate> dailyBehaviorRebateList = dailyBehaviorRebateDao.queryDailyBehaviorRebate(behaviorType);
        List<DailyBehaviorRebateEntity> resultList=new ArrayList<>();
        for (DailyBehaviorRebate tem:dailyBehaviorRebateList){
            DailyBehaviorRebateEntity result = DailyBehaviorRebateEntity.builder()
                    .behaviorType(tem.getBehaviorType())
                    .rebateDesc(tem.getRebateDesc())
                    .rebateType(tem.getRebateType())
                    .rebateConfig(tem.getRebateConfig())
                    .build();
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public void saveUseRebateRecord(List<BehaviorRebateAggregate> aggregateList) {
        for (BehaviorRebateAggregate tem : aggregateList) {
            //构建 用户返利订单 对象
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = tem.getBehaviorRebateOrderEntity();
            UserBehaviorRebateOrder userBehaviorRebateOrder = UserBehaviorRebateOrder.builder()
                    .userId(behaviorRebateOrderEntity.getUserId())
                    .orderId(behaviorRebateOrderEntity.getOrderId())
                    .behaviorType(behaviorRebateOrderEntity.getBehaviorType())
                    .rebateDesc(behaviorRebateOrderEntity.getRebateDesc())
                    .rebateType(behaviorRebateOrderEntity.getRebateType())
                    .rebateConfig(behaviorRebateOrderEntity.getRebateConfig())
                    .bizId(behaviorRebateOrderEntity.getBizId())
                    .outBusinessNo(behaviorRebateOrderEntity.getOutBusinessNo())
                    .build();
            //构建任务表对象
            TaskEntity taskEntity = tem.getTaskEntity();
            Task task = Task.builder()
                    .userId(taskEntity.getUserId())
                    .topic(taskEntity.getTopic())
                    .message(JSON.toJSONString(taskEntity.getMessage()))
                    .messageId(taskEntity.getMessageId())
                    .state(taskEntity.getState().getCode())
                    .build();
            //写入数据库
            insertToSql(userBehaviorRebateOrder, task);
        }
        //发送MQ消息
        for (BehaviorRebateAggregate tem : aggregateList){
            TaskEntity taskEntity = tem.getTaskEntity();
            Task task = Task.builder()
                    .userId(taskEntity.getUserId())
                    .topic(taskEntity.getTopic())
                    .message(JSON.toJSONString(taskEntity.getMessage()))
                    .messageId(taskEntity.getMessageId())
                    .state(taskEntity.getState().getCode())
                    .build();
            try {
                eventPublisher.publish(task.getTopic(),task.getMessage());
                taskDao.updateTaskSendMessageCompleted(task);
            }catch (Exception e){
                log.error("写入返利记录，发送MQ消息失败 userId{},task.getTopic()",task.getUserId(),task.getTopic());
                taskDao.updateTaskSendMessageFail(task);
            }


        }
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByBusinessNo(String userId, String outBusinessNo) {
        //1 请求对象
        UserBehaviorRebateOrder tem = new UserBehaviorRebateOrder();
        tem.setUserId(userId);
        tem.setOutBusinessNo(outBusinessNo);
        //2 查询结果
        List<UserBehaviorRebateOrder> temList=userBehaviorRebateOrderDao.queryOrderByBusinessNo(tem);
        if (temList.isEmpty()){
            return null;
        }


        List<BehaviorRebateOrderEntity> resultList=new ArrayList<>();
        for (UserBehaviorRebateOrder tem1:temList) {
            BehaviorRebateOrderEntity result = BehaviorRebateOrderEntity.builder()
                    .userId(tem1.getUserId())
                    .orderId(tem1.getOrderId())
                    .behaviorType(tem1.getBehaviorType())
                    .rebateDesc(tem1.getRebateDesc())
                    .rebateType(tem1.getRebateType())
                    .rebateConfig(tem1.getRebateConfig())
                    .bizId(tem1.getBizId())
                    .outBusinessNo(tem1.getOutBusinessNo())
                    .build();
            resultList.add(result);
        }
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertToSql(UserBehaviorRebateOrder userBehaviorRebateOrder, Task task) {
        try {
            //写入 用户返利订单数据库
            userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);
            //写入任务表
            taskDao.insert(task);
        } catch (AppException appException) {
            log.info(appException.getInfo());
            throw appException;
        } catch (Exception e) {
            log.error("用户已完成今天已完成签到 userId:{} ",userBehaviorRebateOrder.getUserId());
            throw e;
        }


    }
}
