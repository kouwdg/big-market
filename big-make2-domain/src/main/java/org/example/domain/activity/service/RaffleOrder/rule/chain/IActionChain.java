package org.example.domain.activity.service.RaffleOrder.rule.chain;


import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/23 14:22
 */
public interface IActionChain {

    IActionChain next();
    IActionChain appendNext(IActionChain next);
    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}
