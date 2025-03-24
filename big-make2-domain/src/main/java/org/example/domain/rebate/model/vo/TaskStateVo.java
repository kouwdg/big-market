package org.example.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 14:33
 */
@Getter

@AllArgsConstructor
public enum TaskStateVo {
    create("create","创建"),
    complete("complete","完成"),
    fail("fail","失败");

    private final String code;
    private final String desc;
}
