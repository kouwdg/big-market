package org.example.domain.credit.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.credit.model.entity.CreditAccountEntity;
import org.example.domain.credit.model.entity.CreditOrderEntity;

import java.math.BigDecimal;
import java.util.function.BiFunction;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/27 17:04
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeAggregate {
    private String userId;
    private CreditAccountEntity creditAccountEntity;
    private CreditOrderEntity creditOrderEntity;

    public static CreditAccountEntity CreateCreditAccountEntity(String userId, BigDecimal adjustAmount) {
        return CreditAccountEntity.builder()
                .userId(userId)
                .adjustAmount(adjustAmount)
                .build();
    }

    public static CreditOrderEntity CreateCreditOrderEntity
            (String userId,
             String tradeName,
             String tradeType,
             BigDecimal tradeAmount,
             String outBusinessNo) {
        return CreditOrderEntity.builder()
                .userId(userId)
                .orderId(RandomStringUtils.randomNumeric(12))
                .tradeAmount(tradeAmount)
                .tradeName(tradeName)
                .tradeType(tradeType)
                .outBusinessNo(outBusinessNo)
                .build();
    }
}
