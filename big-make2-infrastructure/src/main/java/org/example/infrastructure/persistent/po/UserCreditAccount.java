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
 * @description: 用户积分表
 * @date 2025/3/25 16:56
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreditAccount {
    private Long id;
    private String userId;
    //总积分
    private BigDecimal totalAmount;
    //可用积分
    private BigDecimal availableAmount;
    //账户状态 open 可用 close冻结
    private String accountStatus;
    // 创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}
