package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/1/13 21:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleStrategyRuleWeightResponseDTO {

    //抽奖额度的次数
    private Integer ruleWeightCount;
    //用户完成的抽奖次数
    private Integer userActivityAccountTotalUserCount;

    //奖品
    private List<StrategyAward> strategyAwards;

    @Data
    public static class StrategyAward{
        private Integer awardId;
        private String awardTitle;
    }
}
