package com.cheng.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户抽奖订单的状态枚举
 */
@AllArgsConstructor
@Getter
public enum UserRaffleOrderStateVo {

    create( "create","创建"),
    used(  "used","已使用"),
    cancel("cancel","已作废");
    private final String code;
    private final String desc;
}
