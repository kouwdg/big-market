package org.example.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/1/3 22:51
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVo {
    SKU("sku", "活动充值商品"),
    INTEGRAL("integral", "用户活动积分");

    private String code;
    private String info;
}
