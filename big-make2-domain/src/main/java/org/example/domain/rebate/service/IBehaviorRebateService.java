package org.example.domain.rebate.service;



import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.entity.DailyBehaviorRebateEntity;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 行为返利服务接口
 * @date 2024/12/30 18:40
 */
public interface IBehaviorRebateService {


    /**
     *
     * 写入数据到 返利订单流水表 中
     * 写入task表，发送MQ消息
     * @param behaviorEntity
     * @return
     */
    List<String> createOrder(BehaviorEntity behaviorEntity);


    //查询返利订单
    List<BehaviorRebateOrderEntity> queryOrderByBusinessNo(String userId, String outBusinessNo);

}
