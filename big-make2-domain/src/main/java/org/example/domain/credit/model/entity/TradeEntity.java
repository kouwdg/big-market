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
 * @date 2025/3/27 16:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeEntity {
    private String userId;
    private BigDecimal adjustAmount;
    private String tradeName;
    private String tradeType;
    private String outBusinessNo;
}
