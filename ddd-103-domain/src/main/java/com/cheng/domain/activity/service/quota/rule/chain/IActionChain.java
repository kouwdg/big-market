package com.cheng.domain.activity.service.quota.rule.chain;

import com.cheng.domain.activity.model.entity.ActivityCountEntity;
import com.cheng.domain.activity.model.entity.ActivityEntity;
import com.cheng.domain.activity.model.entity.ActivitySkuEntity;

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
