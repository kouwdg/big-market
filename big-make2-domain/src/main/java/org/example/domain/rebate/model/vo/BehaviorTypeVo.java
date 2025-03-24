package org.example.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/30 18:42
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVo {
    SIGN("sign", "签到"),
    OPENAL_PAY("openai_pay", "外部支付完成");

    private String code;
    private String info;
}
