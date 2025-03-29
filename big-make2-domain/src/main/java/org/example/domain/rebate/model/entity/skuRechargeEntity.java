package org.example.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/23 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class skuRechargeEntity {
    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;
    //业务单号
    private String outBusinessNo;

    //订单ID
    private String skuConfig;
}
