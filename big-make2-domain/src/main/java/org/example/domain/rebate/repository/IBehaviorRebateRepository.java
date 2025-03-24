package org.example.domain.rebate.repository;


import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.entity.DailyBehaviorRebateEntity;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 行为返利服务仓储接口
 * @date 2024/12/30 18:50
 */
public interface IBehaviorRebateRepository {
    //查询返利配置
    List<DailyBehaviorRebateEntity> queryDailyBehaviorRebate(String code);

    //保存聚合对象  保存返利订单 保存take对象
    void saveUseRebateRecord(List<BehaviorRebateAggregate> aggregateList);

    List<BehaviorRebateOrderEntity> queryOrderByBusinessNo(String userId, String outBusinessNo);
}
