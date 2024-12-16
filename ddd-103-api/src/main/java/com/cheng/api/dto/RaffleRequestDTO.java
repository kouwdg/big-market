package com.cheng.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/15 21:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleRequestDTO {
    //抽奖策略ID
    private Long strategyId;
}
