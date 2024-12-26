package com.cheng.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserAwardRecordStateVo {

    create("create","创建"),
    complete("complete","发奖完成"),
    fail("fail","发奖失败");

    private final String code;
    private final String desc;
}
