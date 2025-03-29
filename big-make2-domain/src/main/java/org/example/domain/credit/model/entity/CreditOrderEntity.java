package org.example.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 程宇乐
 * @version 1.0
 * @description:
 * @date 2025/3/27 17:03
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditOrderEntity {
    private String userId;
    private String orderId;
    private String tradeName;
    private String tradeType;
    private BigDecimal tradeAmount;
    private String outBusinessNo;
}
