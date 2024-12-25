package com.cheng.domain.activity.service.armory;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 装配sku到redis
 * @date 2024/12/24 14:30
 */
public interface IActivityArmory {

    boolean assembleActivitySku(Long sku);
}
