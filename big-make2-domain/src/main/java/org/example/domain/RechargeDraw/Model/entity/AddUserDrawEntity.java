package org.example.domain.RechargeDraw.Model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/28 16:57
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddUserDrawEntity {
    //1 用户抽奖次数订单表Id
    private String orderId;
    //2 用户ID
    private String userId;
    //3 活动ID
    private Long activityId;
    //4 添加的次数
    private Integer count;
}
