package org.example.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户抽奖单 入参对象
 * @date 2024/12/25 18:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartakeRaffleActivityEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动Id
     */
    private Long activityId;
}
