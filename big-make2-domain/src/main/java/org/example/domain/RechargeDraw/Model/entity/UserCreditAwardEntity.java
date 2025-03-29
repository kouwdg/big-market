package org.example.domain.RechargeDraw.Model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户积分实体对象 用来加积分制的对象
 *  告诉要给用户加多少积分
 * @date 2025/3/25 17:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreditAwardEntity {
    private String userId;
    private BigDecimal creditAmount;
}
