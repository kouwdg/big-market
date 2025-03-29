package org.example.domain.credit.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 交易类型
 * @date 2025/3/27 16:55
 */
@Getter
@AllArgsConstructor
public enum TradeTypeVo {
    REBATE("行为返利"),
    CONVERT_SKU("兑换抽奖");

    private final String name;
}
