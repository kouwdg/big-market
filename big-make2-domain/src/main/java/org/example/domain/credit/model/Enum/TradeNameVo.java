package org.example.domain.credit.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeNameVo {

    REBATE("行为返利"),
    CONVERT_SKU("兑换抽奖");

    private final String name;
}
