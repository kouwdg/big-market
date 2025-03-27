package org.example.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.entity.UserCreditAwardEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 发放奖品的聚合对象
 * @date 2025/3/25 17:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiveOutPrizesAggregate {

    //用户ID
    private String userId;
    //用户发奖记录
    private UserAwardRecordEntity userAwardRecordEntity;
    //奖品积分对象
    private UserCreditAwardEntity userCreditAwardEntity;
}
