package org.example.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/27 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreditOrder {
    private Long id;
    private String userId;
    private String orderId;
    private String tradeName;
    private String tradeType;
    private BigDecimal tradeAmount;
    private String outBusinessNo;
    private Date createTime;
    private Date updateTime;
}
