package org.example.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/21 18:15
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleWeightVo {
    //权重对应的值 5000:101,102,103
    private String ruleValue;

    //权重值 5000
    private Integer weight;

    //权重里面对应的 奖品ID
    private List<Integer> awardIds;

    private List<Award>awardList;


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor

    public static class Award{
        private Integer awardId;
        private String awardTitle;

        @Override
        public String toString() {
            return "Award{" +
                    "awardId=" + awardId +
                    ", awardTitle='" + awardTitle + '\'' +
                    '}';
        }
    }
}
