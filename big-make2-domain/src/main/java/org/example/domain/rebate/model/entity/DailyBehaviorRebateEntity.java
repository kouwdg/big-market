package org.example.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/30 18:59
 */
@Getter
@Builder
@AllArgsConstructor
public class DailyBehaviorRebateEntity {

    /** 行为类型（sign 签到、openai_pay 支付） */
    private String behaviorType;
    /** 返利描述 */
    private String rebateDesc;
    /** 返利类型（sku 活动库存充值商品、integral 用户活动积分） */
    private String rebateType;
    /** 返利配置 */
    private String rebateConfig;
}
