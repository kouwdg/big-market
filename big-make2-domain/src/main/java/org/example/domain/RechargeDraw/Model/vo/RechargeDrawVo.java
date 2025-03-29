package org.example.domain.RechargeDraw.Model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 兑换抽奖次数的参数
 * @date 2025/3/28 11:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RechargeDrawVo {
    private String userId;

    //兑换的抽奖数量
    private Integer count;

    private Long sku;

    private Long activityId;
    /**
     * 业务防重Id
     */
    private String outBusinessNo;
}
