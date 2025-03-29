package org.example.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 调额账户积分实体对象
 * @date 2025/3/27 16:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditAccountEntity {
    private String userId;

    //每次扣减或添加的积分值
    private BigDecimal adjustAmount;
}
